package dev.heinzl.simplessoproxy.scripting;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;
import java.util.IllegalFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.stereotype.Component;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;
import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;
import dev.heinzl.simplessoproxy.testing.TestingUtils;
import dev.heinzl.simplessoproxy.utils.LogExecutionTime;

@Component
public class ScriptingApiFactory {

    @Value("${httpclient.ssl.useInsecureContext}")
    boolean useInsecureContext;

    @Autowired
    ScriptEngine scriptEngine;

    @Autowired
    RepositoryFacade repositoryFacade;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @LogExecutionTime
    public ScriptingApi createScriptingApiObject(App app, GatewayFilterSpec gatewayFilterSpec) {

        String proxyScript = app.getProxyScript();

        if (!ScriptValidator.getInstance().isValid(proxyScript)) {
            throw new IllegalArgumentException("Script is not valid!");
        }

        Builder httpClientBuilder = HttpClient.newBuilder();

        if (useInsecureContext) {
            httpClientBuilder.sslContext(TestingUtils.insecureContext());
        }

        ScriptingApi scriptingApi = new ScriptingApiImpl(app, gatewayFilterSpec, repositoryFacade, jwtTokenProvider,
                httpClientBuilder.build());

        scriptEngine.applyScript(proxyScript, scriptingApi);

        return scriptingApi;
    }

}
