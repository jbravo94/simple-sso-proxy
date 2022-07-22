package dev.heinzl.simplessoproxy.scripting;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.configs.JwtTokenProvider;
import dev.heinzl.simplessoproxy.models.App;
import dev.heinzl.simplessoproxy.repositories.RepositoryFacade;
import dev.heinzl.simplessoproxy.scripting.api.ScriptType;
import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;
import dev.heinzl.simplessoproxy.utils.TestingUtils;
import groovy.lang.Closure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ScriptingApiImpl implements ScriptingApi {

    private final App app;
    private final GatewayFilterSpec gatewayFilterSpec;
    private final RepositoryFacade repositoryFacade;
    private final JwtTokenProvider jwtTokenProvider;

    private Map<String, Closure> scriptClosures = new HashMap<>();

    private HttpClient httpClient = HttpClient.newHttpClient();

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

        if (list == null || list.size() == 0 || list.get(0).getValue() == null
                || "null".equals(list.get(0).getValue())) {
            responseHeaders.add("Set-Cookie", String.format("%s=%s; Path=%s", name, value, path));
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
    public String getProxyUsername(ServerWebExchange exchange) {
        return jwtTokenProvider.getUsernameFromRequest(exchange.getRequest());
    }

    @Override
    public String getProxyPassword(ServerWebExchange exchange) {
        String username = jwtTokenProvider.getUsernameFromRequest(exchange.getRequest());
        return this.repositoryFacade.getSecretsRepository().getSecret(username);
    }

    @Override
    public String getAppUsername(ServerWebExchange exchange) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAppPassword(ServerWebExchange exchange) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getAppBaseUrl() {
        return this.app.getBaseUrl();
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
    public void setScript(ScriptType scriptType, Closure closure) {
        setScript(scriptType.name(), closure);
    }

    @Override
    public void setScript(String scriptType, Closure closure) {
        this.scriptClosures.put(scriptType, closure);
    }

    @Override
    public void executeScript(ServerWebExchange exchange, ScriptType scriptType) {
        executeScript(exchange, scriptType.name());
    }

    @Override
    public void executeScript(ServerWebExchange exchange, String scriptType) {
        Closure closure = this.scriptClosures.get(scriptType);

        if (closure != null) {
            closure.call(exchange);
        }
    }

    @Override
    public String executeRequest(HttpRequest request) {

        String body;

        try {
            HttpResponse<String> response = httpClient.send(request,
                    BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e.getMessage());
        }

        return body;
    }

    @Override
    public String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    @Override
    public JSONObject getJSONObjectFromString(String body) {
        return new JSONObject(body);
    }

    @Override
    public void createGatewayFilter(Closure closure) {
        GatewayFilter gatewayFilter = new OrderedGatewayFilter((exchange, chain) -> {

            closure.call(exchange);

            // Disable in production
            TestingUtils.modifyBahmniCookie(exchange);

            return chain.filter(exchange);
        }, Integer.MAX_VALUE);

        gatewayFilterSpec.filter(gatewayFilter);
    }

    // add HTTP build and function for content extraction
}
