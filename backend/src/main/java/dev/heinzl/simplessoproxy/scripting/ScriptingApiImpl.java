package dev.heinzl.simplessoproxy.scripting;

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.web.reactive.function.client.WebClient;

public class ScriptingApiImpl implements ScriptingApi {

    private GatewayFilterSpec gatewayFilterSpec;
    private WebClient client = WebClient.create();

    public ScriptingApiImpl(GatewayFilterSpec gatewayFilterSpec) {
        this.gatewayFilterSpec = gatewayFilterSpec;
    }

    @Override
    public GatewayFilterSpec getGatewayFilterSpec() {
        return this.gatewayFilterSpec;
    }

    @Override
    public void addProxyRequestHeaderIfNotPreset(String key, String value) {
        gatewayFilterSpec.addRequestHeader(key, value);
    }

    @Override
    public void addProxyCookieIfNotPreset(String name, String value) {
        // TODO Auto-generated method stub

    }

    // add HTTP build and function for content extraction
}
