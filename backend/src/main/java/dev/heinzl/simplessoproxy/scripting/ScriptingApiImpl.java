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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;
import dev.heinzl.simplessoproxy.credentials.Credential;
import dev.heinzl.simplessoproxy.scripting.api.ScriptType;
import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;
import dev.heinzl.simplessoproxy.users.User;
import dev.heinzl.simplessoproxy.utils.CookieUtils;
import dev.heinzl.simplessoproxy.utils.TestingUtils;
import groovy.lang.Closure;
import lombok.NonNull;
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

    // Fix this
    private HttpClient httpClient = HttpClient.newBuilder().sslContext(TestingUtils.insecureContext()).build();

    @Override
    public void addProxyRequestHeaderIfNotPreset(ServerWebExchange exchange, String key, String value) {
        exchange.getRequest().getHeaders().addIfAbsent(key, value);
    }

    @Override
    public void addProxyResponseHeaderIfNotPreset(ServerWebExchange exchange, String key, String value) {
        exchange.getResponse().getHeaders().addIfAbsent(key, value);
    }

    @Override
    public void addProxyRequestCookieIfNotPreset(ServerWebExchange exchange, String name, String value) {
        exchange.getRequest().getCookies().addIfAbsent(name, new HttpCookie(name, value));
    }

    @Override
    public void addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value) {
        addProxyResponseSetCookieIfNotPresentInRequest(exchange, name, value, "/");
    }

    @Override
    public void addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value,
            @NonNull String path) {

        MultiValueMap<String, HttpCookie> requestCookies = exchange.getRequest().getCookies();
        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();

        List<HttpCookie> list = requestCookies.get(name);

        if (CollectionUtils.isEmpty(list)
                || list.stream().allMatch(
                        cookie -> StringUtils.isAllEmpty(cookie.getValue()) || cookie.getValue().equals("null"))) {
            responseHeaders.add(HttpHeaders.SET_COOKIE, String.format("%s=%s; Path=%s", name, value, path));
        }
    }

    @Override
    public void addPermanentProxyRequestHeader(String key, String value) {
        gatewayFilterSpec.addRequestHeader(key, value);
    }

    @Override
    public void addPermanentProxyResponseHeader(String key, String value) {
        gatewayFilterSpec.addResponseHeader(key, value);
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

    Credential getAppCredential(ServerWebExchange exchange) {
        String username = this.getProxyUsername(exchange);
        User user = this.getProxyUser(username);
        return getAppCredential(user);
    }

    @Override
    public String getAppSecret(ServerWebExchange exchange) {
        Credential credential = getAppCredential(exchange);

        return credential == null ? null : credential.getSecret();
    }

    Credential getAppCredential(User user) {

        List<Credential> findByAppIdAndUserId = this.repositoryFacade.getCredentialsRepository()
                .findByAppIdAndUserId(app.getId(), user.getId());

        if (CollectionUtils.isEmpty(findByAppIdAndUserId)) {
            return null;
        }

        if (findByAppIdAndUserId.size() > 1) {
            throw new IllegalStateException("More than one App credential found. Manual fix needed!");
        }

        return findByAppIdAndUserId.get(0);
    }

    @Override
    public void setAppSecret(ServerWebExchange exchange, String secret) {
        String username = this.getProxyUsername(exchange);
        User user = this.getProxyUser(username);
        Credential credential = this.getAppCredential(user);
        updateAppCredential(credential, user, secret);
    }

    User getProxyUser(String username) {
        return this.repositoryFacade.getUsersRepository().findByUsername(username);
    }

    Credential updateAppCredential(Credential credential, User user, String secret) {

        if (credential == null) {
            credential = Credential.builder().app(this.app).secret(secret).user(user).build();
        } else {
            credential.setSecret(secret);
        }

        return this.repositoryFacade.getCredentialsRepository().save(credential);
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
        } else {
            throw new IllegalArgumentException("Script not loaded!");
        }
    }

    @Override
    public HttpResponse<String> executeRequest(HttpRequest request) {

        try {
            HttpResponse<String> response = httpClient.send(request,
                    BodyHandlers.ofString());

            log.info(String.format("Request headers are %s", request.headers().toString()));
            log.info(String.format("Response code is %d", response.statusCode()));
            log.info(String.format("Response headers are %s", response.headers().toString()));
            log.info(String.format("Response body is %s", response.body()));

            return response;

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public String getBasicAuthenticationHeader(@NonNull String username, @NonNull String password) {
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

            // Fix this - Disable in production
            TestingUtils.modifyBahmniCookie(exchange);

            return chain.filter(exchange);
        }, Integer.MAX_VALUE);

        gatewayFilterSpec.filter(gatewayFilter);
    }
}
