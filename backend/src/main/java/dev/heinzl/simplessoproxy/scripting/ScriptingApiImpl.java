package dev.heinzl.simplessoproxy.scripting;

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.web.reactive.function.client.WebClient;

import dev.heinzl.simplessoproxy.models.App;

public class ScriptingApiImpl implements ScriptingApi {

    private App app;
    private GatewayFilterSpec gatewayFilterSpec;
    private WebClient client = WebClient.create();

    public ScriptingApiImpl(App app, GatewayFilterSpec gatewayFilterSpec) {
        this.app = app;
        this.gatewayFilterSpec = gatewayFilterSpec;
    }

    @Override
    public void addProxyRequestHeaderIfNotPreset(String key, String value) {
        gatewayFilterSpec.addRequestHeader(key, value);
    }

    @Override
    public void addProxyCookieIfNotPreset(String name, String value) {
        // TODO Auto-generated method stub

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

    // add HTTP build and function for content extraction
}
