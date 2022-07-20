package dev.heinzl.simplessoproxy.scripting;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.models.App;
import dev.heinzl.simplessoproxy.repositories.RepositoryFacade;
import groovy.lang.Closure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ScriptingApiImpl implements ScriptingApi {

    private final App app;
    private final GatewayFilterSpec gatewayFilterSpec;
    private final RepositoryFacade repositoryFacade;
    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository serverSecurityContextRepository;

    private WebClient client = WebClient.create();

    @Override
    public void addProxyRequestHeaderIfNotPreset(ServerWebExchange exchange, String key, String value) {
        // TODO Fix
        exchange.getRequest().getHeaders().set(key, value);
    }

    @Override
    public void addProxyResponseHeaderIfNotPreset(ServerWebExchange exchange, String key, String value) {
        // TODO Fix
        exchange.getResponse().getHeaders().set(key, value);
    }

    @Override
    public void addProxyRequestCookieIfNotPreset(ServerWebExchange exchange, String name, String value) {
        // TODO Fix
        exchange.getRequest().getHeaders().set("Cookie", String.format("%s=%s", name, value));
    }

    @Override
    public void addProxyResponseCookieIfNotPreset(ServerWebExchange exchange, String name, String value, String path) {

        if (path == null) {
            path = "/";
        }

        MultiValueMap<String, HttpCookie> requestCookies = exchange.getRequest().getCookies();
        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();

        List<HttpCookie> list = requestCookies.get(name);

        if (list == null || list.size() == 0) {
            responseHeaders.set("Set-Cookie", String.format("%s=%s; Path=%s", name, value, path));
        }
    }

    @Override
    public void addPermanentProxyRequestHeaderIfNotPreset(String key, String value) {
        // TODO Fix
        gatewayFilterSpec.setRequestHeader(key, value);
    }

    @Override
    public void addPermanentProxyResponseHeaderIfNotPreset(String key, String value) {
        // TODO Fix
        gatewayFilterSpec.setRequestHeader(key, value);
    }

    @Override
    public void addPermanentProxyRequestCookieIfNotPreset(String name, String value) {
        // TODO Fix
        gatewayFilterSpec.setRequestHeader("Cookie", String.format("%s=%s", name, value));
    }

    @Override
    public void addPermanentProxyResponseCookieIfNotPreset(String name, String value, String path) {
        if (path == null) {
            path = "/";
        }

        gatewayFilterSpec.addResponseHeader("Set-Cookie", String.format("%s=%s; Path=%s", name, value, path));
    }

    @Override
    public String getProxyUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null;
    }

    @Override
    public String getProxyPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAppUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAppPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void logInfo(String text) {
        log.info(text);
    }

    @Override
    public RepositoryFacade getRepositoryFacade() {
        return this.repositoryFacade;
    }

    @Override
    public WebClient getWebClient() {
        return this.client;
    }

    @Override
    public void createGatewayFilter(Closure closure) {
        GatewayFilter gatewayFilter = new OrderedGatewayFilter((exchange, chain) -> {
            Mono<SecurityContext> load = serverSecurityContextRepository.load(exchange);

            load.flatMap(t -> {
                log.info(t.getAuthentication().getPrincipal().toString());
                return Mono.just(t);
            });

            closure.call(exchange);
            return chain.filter(exchange);
        }, 0);

        gatewayFilterSpec.filter(gatewayFilter);
    }

    // add HTTP build and function for content extraction
}
