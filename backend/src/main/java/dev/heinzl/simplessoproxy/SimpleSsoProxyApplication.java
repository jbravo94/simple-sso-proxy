package dev.heinzl.simplessoproxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import dev.heinzl.simplessoproxy.apps.AppsRepository;
import dev.heinzl.simplessoproxy.configs.routing.ApiPathRouteLocatorImpl;
import dev.heinzl.simplessoproxy.scripting.ScriptingApiFactory;
import lombok.Generated;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAspectJAutoProxy(proxyTargetClass = true)
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
