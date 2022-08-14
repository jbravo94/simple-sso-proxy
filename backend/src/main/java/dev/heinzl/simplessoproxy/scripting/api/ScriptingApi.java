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

/**
 * This class represents the scripting API contract.
 * It is intended to be used in the UI's scripting editor.
 * It serves the purpose for public documentation and interface for
 * implementations.
 * All functions here should be available as autocompletion in the UI's
 * scripting editor.
 * Current usual behavior is that runtime exception will be thrown in case of
 * error to abort.
 * 
 * @author Johannes HEINZL
 * @author heinzl.dev
 */
public interface ScriptingApi {

    /**
     * This method adds a proxy request header if not present to a specified http
     * exchange.
     * 
     * @param exchange represents the http communication object
     * @param key      is the header name
     * @param value    is the header value
     */
    void addProxyRequestHeaderIfNotPreset(ServerWebExchange exchange, String key,
            String value);

    /**
     * This method adds a proxy response header if not present to a specified http
     * exchange.
     * 
     * @param exchange represents the http communication object
     * @param key      is the header name
     * @param value    is the header value
     */
    void addProxyResponseHeaderIfNotPreset(ServerWebExchange exchange, String key, String value);

    /**
     * This method adds a proxy request cookie if not present to a specified http
     * exchange.
     * 
     * @param exchange represents the http communication object
     * @param name     is the cookie name
     * @param value    is the cookie value
     */
    void addProxyRequestCookieIfNotPreset(ServerWebExchange exchange, String name, String value);

    /**
     * This method adds a proxy response cookie if not present to a specified http
     * exchange.
     * The cookie path defaults here to "/".
     * 
     * @param exchange represents the http communication object
     * @param name     is the cookie name
     * @param value    is the cookie value
     */
    void addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value);

    /**
     * This method adds a proxy response cookie if not present to a specified http
     * exchange.
     * 
     * @param exchange represents the http communication object
     * @param name     is the cookie name
     * @param value    is the cookie value
     * @param path     is the applicable path for the cookie
     */
    void addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value,
            String path);

    /**
     * This method adds a proxy request header to all http exchanges.
     * 
     * @param key   is the header name
     * @param value is the header value
     */
    void addPermanentProxyRequestHeader(String key, String value);

    /**
     * This method adds a proxy response header to all http exchanges.
     * 
     * @param key   is the header name
     * @param value is the header value
     */
    void addPermanentProxyResponseHeader(String key, String value);

    /**
     * This method returns current logged in user of the proxy backend.
     * It extracts the username from a JWT value provided by the proxy cookie set
     * during authentication.
     * 
     * @param exchange represents the http communication object
     * @return the username
     */
    String getProxyUsername(ServerWebExchange exchange);

    /**
     * This method returns the password of the current logged in user of the proxy
     * backend.
     * The password is normally stored temporily and is retrieved with the username
     * by utilizing {@link #getProxyUsername(ServerWebExchange exchange)}.
     * It returns null if either no password is available or the temporarily
     * password store has it expired already
     * 
     * @param exchange represents the http communication object
     * @return the username or null
     */
    String getProxyPassword(ServerWebExchange exchange);

    /**
     * Gets an app secret (e.g. session token) from a secret store.
     * Http exchange is needed to determine the user.
     * 
     * @param exchange represents the http communication object
     * @return secret
     */
    String getAppSecret(ServerWebExchange exchange);

    /**
     * Stores an app secret (e.g. session token) in a secret store.
     * Http exchange is needed to determine the user.
     * 
     * @param exchange represents the http communication object
     * @param secret   to be stored
     */
    void setAppSecret(ServerWebExchange exchange, String secret);

    /**
     * Returns the app base url.
     * 
     * @return app base url
     */
    String getAppBaseUrl();

    /**
     * Returns Logger instance.
     * 
     * @return org.slf4j.Logger instance
     */
    Logger getLogger();

    /**
     * Logs provided text with info level in default logger.
     * 
     * @param text to be logged
     */
    void logInfo(String text);

    /**
     * Returns an instance of RepositoryFacade which provides bundled access to
     * several repositories
     * 
     * @return Instance of RepositoryFacade
     */
    RepositoryFacade getRepositoryFacade();

    /**
     * Takes http request object which contains all information needed to execute
     * the request.
     * It executes the request in a blocking manner.
     * 
     * @param request java.net.http.HttpRequest object
     * @return java.net.http.HttpResponse object with body as string
     */
    HttpResponse<String> executeRequest(HttpRequest request);

    /**
     * Utility method to generate a basic authentication header.
     * 
     * @param username is the username as string
     * @param password is the password as string
     * @return basic auth header
     */
    String getBasicAuthenticationHeader(String username, String password);

    /**
     * Utility method to convert a json string to a JSONObject.
     * 
     * @param body as json string
     * @return org.json.JSONObject on successful conversion
     */
    JSONObject getJSONObjectFromString(String body);

    /**
     * Creates gatewayfilter with provided closure object which need to be created
     * and provided in the scripting editor.
     * 
     * @param closure groovy.lang.Closure as function with scripted custom logic
     */
    void createGatewayFilter(Closure closure);

    /**
     * Set script in internal script repository for script with a default type.
     * 
     * @param scriptType from enum of default script types
     * @param closure    groovy.lang.Closure as function with scripted custom logic
     */
    void setScript(ScriptType scriptType, Closure closure);

    /**
     * Set script in internal script repository for script with a custom type.
     * 
     * @param scriptType as custom script type
     * @param closure    groovy.lang.Closure as function with scripted custom logic
     */
    void setScript(String scriptType, Closure closure);

    /**
     * Executed script from internal script repository for script by providing a
     * default type.
     * 
     * @param exchange   represents the http communication object which triggers the
     *                   execution
     * @param scriptType from enum of default script types
     */
    void executeScript(ServerWebExchange exchange, ScriptType scriptType);

    /**
     * Executed script from internal script repository for script by providing a
     * custom type.
     * 
     * @param exchange   represents the http communication object which triggers the
     *                   execution
     * @param scriptType as custom script type
     */
    void executeScript(ServerWebExchange exchange, String scriptType);
}
