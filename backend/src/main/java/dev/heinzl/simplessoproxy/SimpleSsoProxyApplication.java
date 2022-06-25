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

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}
}
