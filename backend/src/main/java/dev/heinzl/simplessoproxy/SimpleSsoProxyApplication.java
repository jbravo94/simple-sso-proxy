package dev.heinzl.simplessoproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import dev.heinzl.simplessoproxy.apps.AppsRepository;
import dev.heinzl.simplessoproxy.configs.routing.ApiPathRouteLocatorImpl;
import dev.heinzl.simplessoproxy.scripting.ScriptingApiFactory;
import lombok.Generated;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SimpleSsoProxyApplication {

	@Generated
	public static void main(String[] args) {
		SpringApplication.run(SimpleSsoProxyApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder, AppsRepository appsRepository,
			ScriptingApiFactory scriptingApiFactory) {
		return new ApiPathRouteLocatorImpl(routeLocatorBuilder, appsRepository, scriptingApiFactory);
	}
}
