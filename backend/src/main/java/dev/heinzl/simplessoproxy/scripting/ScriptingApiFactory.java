package dev.heinzl.simplessoproxy.scripting;

import java.util.IllegalFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
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

        String proxyScript = app.getProxyScript();

        if (!ScriptValidator.getInstance().isValid(proxyScript)) {
            throw new IllegalArgumentException("Script is not valid!");
        }

        ScriptingApi scriptingApi = new ScriptingApiImpl(app, gatewayFilterSpec, repositoryFacade, jwtTokenProvider);

        scriptEngine.applyScript(proxyScript, scriptingApi);

        return scriptingApi;
    }

}
