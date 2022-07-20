package dev.heinzl.simplessoproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.heinzl.simplessoproxy.configs.ApiPathRouteLocatorImpl;
import dev.heinzl.simplessoproxy.repositories.AppsRepository;
import dev.heinzl.simplessoproxy.repositories.PersistentCredentialsRepository;
import dev.heinzl.simplessoproxy.repositories.RepositoryFacade;
import dev.heinzl.simplessoproxy.repositories.SecretsRepository;
import dev.heinzl.simplessoproxy.repositories.UsersRepository;
import dev.heinzl.simplessoproxy.scripting.ScriptingApiFactory;
import dev.heinzl.simplessoproxy.services.ScriptEngine;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@ConfigurationPropertiesScan
public class SimpleSsoProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSsoProxyApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder, AppsRepository appsRepository,
			ScriptingApiFactory scriptingApiFactory) {
		return new ApiPathRouteLocatorImpl(routeLocatorBuilder, appsRepository, scriptingApiFactory);
	}

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}
}
