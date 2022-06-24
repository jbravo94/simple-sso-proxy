package dev.heinzl.simplessoproxy.scripting;

import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;

public interface ScriptingApi {
    void addProxyRequestHeaderIfNotPreset(String key, String value);

    void addProxyCookieIfNotPreset(String name, String value);

    GatewayFilterSpec getGatewayFilterSpec();
}
