package dev.heinzl.simplessoproxy.configs;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import dev.heinzl.simplessoproxy.models.App;
import dev.heinzl.simplessoproxy.repositories.AppsRepository;
import dev.heinzl.simplessoproxy.scripting.ScriptingApi;
import dev.heinzl.simplessoproxy.scripting.ScriptingApiImpl;
import dev.heinzl.simplessoproxy.services.ScriptEngine;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ApiPathRouteLocatorImpl implements RouteLocator {
  private final ScriptEngine scriptEngine;
  private final AppsRepository appsRepository;
  private final RouteLocatorBuilder routeLocatorBuilder;

  @Override
  public Flux<Route> getRoutes() {
    RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
    return Flux.fromIterable(appsRepository.findAll())
        .map(app -> routesBuilder.route(predicateSpec -> setPredicateSpec(app, predicateSpec)))
        .collectList().flatMapMany(builders -> routesBuilder.build()
            .getRoutes());
  }

  private Buildable<Route> setPredicateSpec(App app, PredicateSpec predicateSpec) {
    BooleanSpec booleanSpec = predicateSpec.alwaysTrue();

    booleanSpec.filters(gatewayFilterSpec -> {
      ScriptingApi scriptingApi = new ScriptingApiImpl(app, gatewayFilterSpec);

      scriptEngine.applyScript(app.getProxyScript(), scriptingApi);

      return gatewayFilterSpec;
    });

    return booleanSpec.uri(app.getBaseUrl());
  }
}