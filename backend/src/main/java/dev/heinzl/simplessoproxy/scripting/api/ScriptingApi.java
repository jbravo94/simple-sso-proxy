package dev.heinzl.simplessoproxy.scripting.api;

import java.net.http.HttpRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
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

    String getAppCredential(ServerWebExchange exchange);

    void setAppCredential(ServerWebExchange exchange, String secret);

    String getAppBaseUrl();

    Logger getLogger();

    void logInfo(String text);

    RepositoryFacade getRepositoryFacade();

    String executeRequest(HttpRequest request);

    String getBasicAuthenticationHeader(String username, String password);

    JSONObject getJSONObjectFromString(String body);

    void createGatewayFilter(Closure closure);

    void setScript(ScriptType scriptType, Closure closure);

    void setScript(String scriptType, Closure closure);

    void executeScript(ServerWebExchange exchange, ScriptType scriptType);

    void executeScript(ServerWebExchange exchange, String scriptType);
}
