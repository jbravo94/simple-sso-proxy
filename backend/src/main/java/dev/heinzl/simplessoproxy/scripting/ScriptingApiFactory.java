package dev.heinzl.simplessoproxy.scripting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;
import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;

@Component
public class ScriptingApiFactory {

    @Autowired
    ScriptEngine scriptEngine;

    @Autowired
    RepositoryFacade repositoryFacade;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public ScriptingApi createScriptingApiObject(App app, GatewayFilterSpec gatewayFilterSpec) {

        ScriptingApi scriptingApi = new ScriptingApiImpl(app, gatewayFilterSpec, repositoryFacade, jwtTokenProvider);

        scriptEngine.applyScript(app.getProxyScript(), scriptingApi);

        return scriptingApi;
    }

}
