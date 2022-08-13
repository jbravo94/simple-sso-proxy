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
package dev.heinzl.simplessoproxy.scripting.api;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.scripting.RepositoryFacade;
import groovy.lang.Closure;

public interface ScriptingApi {
    void addProxyRequestHeaderIfNotPreset(ServerWebExchange exchange, String key,
            String value);

    void addProxyResponseHeaderIfNotPreset(ServerWebExchange exchange, String key, String value);

    void addProxyRequestCookieIfNotPreset(ServerWebExchange exchange, String name, String value);

    void addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value);

    void addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value,
            String path);

    void addPermanentProxyRequestHeader(String key, String value);

    void addPermanentProxyResponseHeader(String key, String value);

    String getProxyUsername(ServerWebExchange exchange);

    String getProxyPassword(ServerWebExchange exchange);

    String getAppSecret(ServerWebExchange exchange);

    void setAppSecret(ServerWebExchange exchange, String secret);

    String getAppBaseUrl();

    Logger getLogger();

    void logInfo(String text);

    RepositoryFacade getRepositoryFacade();

    HttpResponse<String> executeRequest(HttpRequest request);

    String getBasicAuthenticationHeader(String username, String password);

    JSONObject getJSONObjectFromString(String body);

    void createGatewayFilter(Closure closure);

    void setScript(ScriptType scriptType, Closure closure);

    void setScript(String scriptType, Closure closure);

    void executeScript(ServerWebExchange exchange, ScriptType scriptType);

    void executeScript(ServerWebExchange exchange, String scriptType);
}
