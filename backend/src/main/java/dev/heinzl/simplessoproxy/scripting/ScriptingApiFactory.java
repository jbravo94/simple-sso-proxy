/*
 * The MIT License
 * Copyright © 2022 Johannes HEINZL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.heinzl.simplessoproxy.scripting;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;

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

    @Value("${httpclient.ssl.useInsecureContext:false}")
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
