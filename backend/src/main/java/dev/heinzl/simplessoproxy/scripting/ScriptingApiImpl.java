package dev.heinzl.simplessoproxy.scripting;

import java.util.StringJoiner;

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.web.reactive.function.client.WebClient;

import dev.heinzl.simplessoproxy.models.App;
import dev.heinzl.simplessoproxy.repositories.RepositoryFacade;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScriptingApiImpl implements ScriptingApi {

    private final App app;
    private final GatewayFilterSpec gatewayFilterSpec;
    private final RepositoryFacade repositoryFacade;
    private final ReactiveAuthenticationManager authenticationManager;

    private WebClient client = WebClient.create();

    @Override
    public void addProxyRequestHeaderIfNotPreset(String key, String value) {
        gatewayFilterSpec.addRequestHeader(key, value);
    }

    @Override
    public void addProxyResponseHeaderIfNotPreset(String key, String value) {
        gatewayFilterSpec.addResponseHeader(key, value);
    }

    @Override
    public void addProxyRequestCookieIfNotPreset(String name, String value) {
        gatewayFilterSpec.addRequestHeader("Cookie", String.format("%s=%s", name, value));
    }

    @Override
    public String getProxyUsername() {
        // TODO Auto-generated method stub
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
    public void addProxyResponseCookieIfNotPreset(String name, String value, String path) {
        if (path == null) {
            path = "/";
        }

        gatewayFilterSpec.addResponseHeader("Set-Cookie", String.format("%s=%s; Path=%s", name, value, path));
    }

    // add HTTP build and function for content extraction
}
