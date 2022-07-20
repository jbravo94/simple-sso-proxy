package dev.heinzl.simplessoproxy.scripting;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.repositories.RepositoryFacade;
import groovy.lang.Closure;

public interface ScriptingApi {
    void addProxyRequestHeaderIfNotPreset(ServerWebExchange exchange, String key, String value);

    void addProxyResponseHeaderIfNotPreset(ServerWebExchange exchange, String key, String value);

    void addProxyRequestCookieIfNotPreset(ServerWebExchange exchange, String name, String value);

    void addProxyResponseCookieIfNotPreset(ServerWebExchange exchange, String name, String value, String path);

    void addPermanentProxyRequestHeaderIfNotPreset(String key, String value);

    void addPermanentProxyResponseHeaderIfNotPreset(String key, String value);

    void addPermanentProxyRequestCookieIfNotPreset(String name, String value);

    void addPermanentProxyResponseCookieIfNotPreset(String name, String value, String path);

    String getProxyUsername(ServerWebExchange exchange);

    String getProxyPassword(ServerWebExchange exchange);

    String getAppUsername(ServerWebExchange exchange);

    String getAppPassword(ServerWebExchange exchange);

    Logger getLogger();

    void logInfo(String text);

    RepositoryFacade getRepositoryFacade();

    WebClient getWebClient();

    void createGatewayFilter(Closure closure);
}
