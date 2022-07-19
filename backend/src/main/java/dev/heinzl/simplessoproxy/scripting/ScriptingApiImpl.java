package dev.heinzl.simplessoproxy.scripting;

import java.util.StringJoiner;

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.web.reactive.function.client.WebClient;

import dev.heinzl.simplessoproxy.models.App;
import dev.heinzl.simplessoproxy.repositories.RepositoryFacade;

public class ScriptingApiImpl implements ScriptingApi {

    private App app;
    private GatewayFilterSpec gatewayFilterSpec;
    private WebClient client = WebClient.create();
    private RepositoryFacade repositoryFacade;

    public ScriptingApiImpl(App app, GatewayFilterSpec gatewayFilterSpec, RepositoryFacade repositoryFacade) {
        this.app = app;
        this.gatewayFilterSpec = gatewayFilterSpec;
        this.repositoryFacade = repositoryFacade;
    }

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
    public String getUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPassword() {
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
