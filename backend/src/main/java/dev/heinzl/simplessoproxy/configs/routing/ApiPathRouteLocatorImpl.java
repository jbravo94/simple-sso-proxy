package dev.heinzl.simplessoproxy.configs.routing;

import lombok.AllArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;

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
    var t = Flux.fromIterable(appsRepository.findAll())
        .filter(app -> AppValidator.getInstance().isValid(app))
        .map(app -> routesBuilder.route(predicateSpec -> setPredicateSpec(app, predicateSpec)))
        .collectList().flatMapMany(builders -> routesBuilder.build()
            .getRoutes());

    return t;
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