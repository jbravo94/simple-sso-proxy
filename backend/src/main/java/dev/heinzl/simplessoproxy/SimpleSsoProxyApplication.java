package dev.heinzl.simplessoproxy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.heinzl.simplessoproxy.configs.ApiPathRouteLocatorImpl;
import dev.heinzl.simplessoproxy.repositories.AppsRepository;
import dev.heinzl.simplessoproxy.services.ScriptEngine;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class SimpleSsoProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSsoProxyApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(ScriptEngine scriptEngine,
			AppsRepository appsRepository,
			RouteLocatorBuilder routeLocatorBuilder) {
		return new ApiPathRouteLocatorImpl(scriptEngine, appsRepository, routeLocatorBuilder);
	}

	/*
	 * @Bean
	 * public RouteLocator myRoutes(@Autowired AppsRepository
	 * appsRepository, @Autowired ScriptEngine scriptEngine,
	 * RouteLocatorBuilder builder,
	 * UriConfiguration uriConfiguration) {
	 * String httpUri = uriConfiguration.getHttpbin();
	 * 
	 * /*
	 * appsRepository.save(App.builder().baseUrl("http://httpbin.org:80").
	 * proxyScript("""
	 * scriptingApi.addProxyRequestHeaderIfNotPreset('authorization', 'Basic
	 * YWRtaW46YWRtaW4=');
	 * """).build());
	 *
	 * 
	 * List<App> allApps = appsRepository.findAll();
	 * 
	 * return builder.routes().route(p -> p.alwaysTrue()
	 * .filters(f -> {
	 * 
	 * ScriptingApi scriptingApi = new ScriptingApiImpl(f);
	 * 
	 * String script = """
	 * scriptingApi.addProxyRequestHeaderIfNotPreset('authorization', 'Basic
	 * YWRtaW46YWRtaW4=');
	 * """;
	 * 
	 * scriptEngine.applyScript(script, scriptingApi);
	 * 
	 * return scriptingApi.getGatewayFilterSpec();
	 * })
	 * .uri(httpUri)).build();
	 * /*
	 * return builder.routes()
	 * .route(p -> p.predicate(pp -> {
	 * 
	 * System.out.println(pp.getRequest().getHeaders().get("host"));
	 * return true;
	 * }).and()
	 * .path("/**")
	 * .uri("/get"))
	 * .route(p -> p.predicate(pp -> {
	 * 
	 * System.out.println(pp.getRequest().getHeaders().get("host"));
	 * return false;
	 * }).and()
	 * .path("/get")
	 * .filters(f -> f.addRequestHeader("Hello", "World"))
	 * .uri(httpUri))
	 * .route(p -> p
	 * .path("/get1")
	 * .filters(f -> f.addRequestHeader("Hello", "World"))
	 * .uri(httpUri))
	 * .route(p -> p
	 * .host("*.circuitbreaker.com")
	 * .filters(f -> f
	 * .circuitBreaker(config -> config
	 * .setName("mycmd")
	 * .setFallbackUri("forward:/fallback")))
	 * .uri(httpUri))
	 * .build();
	 *
	 * }
	 */

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}
}

@ConfigurationProperties
class UriConfiguration {

	private String httpbin = "http://httpbin.org:80";

	public String getHttpbin() {
		return httpbin;
	}

	public void setHttpbin(String httpbin) {
		this.httpbin = httpbin;
	}
}