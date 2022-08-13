/*
 * The MIT License
 * Copyright © 2022 Johannes HEINZL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.heinzl.simplessoproxy.configs.routing;

import lombok.AllArgsConstructor;

import java.net.URI;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.apps.AppValidator;
import dev.heinzl.simplessoproxy.apps.AppsRepository;
import dev.heinzl.simplessoproxy.scripting.ScriptingApiFactory;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ApiPathRouteLocatorImpl implements RouteLocator {
  private final RouteLocatorBuilder routeLocatorBuilder;
  private final AppsRepository appsRepository;
  private final ScriptingApiFactory scriptingApiFactory;

  @Override
  public Flux<Route> getRoutes() {
    RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
    var routes = Flux.fromIterable(appsRepository.findAll())
        .filter(app -> !AppValidator.getInstance().isValid(app))
        .map(app -> routesBuilder.route(predicateSpec -> setPredicateSpec(app, predicateSpec)))
        .collectList().flatMapMany(builders -> routesBuilder.build()
            .getRoutes());

    return routes;
  }

  private Buildable<Route> setPredicateSpec(App app, PredicateSpec predicateSpec) {
    URI uri = URI.create(app.getProxyUrl());

    BooleanSpec booleanSpec = predicateSpec.host(uri.getHost());

    booleanSpec.filters(gatewayFilterSpec -> {
      scriptingApiFactory.createScriptingApiObject(app, gatewayFilterSpec);
      return gatewayFilterSpec;
    });

    return booleanSpec.uri(app.getBaseUrl());
  }
}