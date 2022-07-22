package dev.heinzl.simplessoproxy.configs.routing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.security.authentication.ReactiveAuthenticationManager;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.apps.AppsRepository;
import dev.heinzl.simplessoproxy.scripting.RepositoryFacade;
import dev.heinzl.simplessoproxy.scripting.ScriptEngine;
import dev.heinzl.simplessoproxy.scripting.ScriptingApiFactory;
import dev.heinzl.simplessoproxy.scripting.ScriptingApiImpl;
import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;
import dev.heinzl.simplessoproxy.secrets.SecretsRepository;
import dev.heinzl.simplessoproxy.users.UsersRepository;
import reactor.core.publisher.Flux;

@Slf4j
@AllArgsConstructor
public class ApiPathRouteLocatorImpl implements RouteLocator {
  private final RouteLocatorBuilder routeLocatorBuilder;
  private final AppsRepository appsRepository;
  private final ScriptingApiFactory scriptingApiFactory;

  @Override
  public Flux<Route> getRoutes() {
    RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
    var t = Flux.fromIterable(appsRepository.findAll())
        .map(app -> routesBuilder.route(predicateSpec -> setPredicateSpec(app, predicateSpec)))
        .collectList().flatMapMany(builders -> routesBuilder.build()
            .getRoutes());

    return t;
  }

  private Buildable<Route> setPredicateSpec(App app, PredicateSpec predicateSpec) {
    URI uri;

    try {
      uri = new URI(app.getProxyUrl());
    } catch (URISyntaxException e) {
      throw new IllegalStateException(e.getMessage());
    }

    BooleanSpec booleanSpec = predicateSpec.host(uri.getHost());

    booleanSpec.filters(gatewayFilterSpec -> {
      scriptingApiFactory.createScriptingApiObject(app, gatewayFilterSpec);
      return gatewayFilterSpec;
    });

    return booleanSpec.uri(app.getBaseUrl());
  }
}