/*
The MIT License
Copyright © 2022 Johannes HEINZL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
export const scriptingApiSuggestions = [
    {
        "insertText": "getLogger()",
        "kind": 1,
        "label": "getLogger",
        "detail": "getLogger()"
    },
    {
        "insertText": "setScript(scriptType, closure)",
        "kind": 1,
        "label": "setScript",
        "detail": "setScript(String scriptType, Closure closure)"
    },
    {
        "insertText": "setScript(scriptType, closure)",
        "kind": 1,
        "label": "setScript",
        "detail": "setScript(ScriptType scriptType, Closure closure)"
    },
    {
        "insertText": "addProxyRequestHeaderIfNotPreset(exchange, key, value)",
        "kind": 1,
        "label": "addProxyRequestHeaderIfNotPreset",
        "detail": "addProxyRequestHeaderIfNotPreset(ServerWebExchange exchange, String key, String value)"
    },
    {
        "insertText": "addProxyResponseHeaderIfNotPreset(exchange, key, value)",
        "kind": 1,
        "label": "addProxyResponseHeaderIfNotPreset",
        "detail": "addProxyResponseHeaderIfNotPreset(ServerWebExchange exchange, String key, String value)"
    },
    {
        "insertText": "addProxyRequestCookieIfNotPreset(exchange, name, value)",
        "kind": 1,
        "label": "addProxyRequestCookieIfNotPreset",
        "detail": "addProxyRequestCookieIfNotPreset(ServerWebExchange exchange, String name, String value)"
    },
    {
        "insertText": "addProxyResponseSetCookieIfNotPresentInRequest(exchange, name, value, path)",
        "kind": 1,
        "label": "addProxyResponseSetCookieIfNotPresentInRequest",
        "detail": "addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value, String path)"
    },
    {
        "insertText": "addProxyResponseSetCookieIfNotPresentInRequest(exchange, name, value)",
        "kind": 1,
        "label": "addProxyResponseSetCookieIfNotPresentInRequest",
        "detail": "addProxyResponseSetCookieIfNotPresentInRequest(ServerWebExchange exchange, String name, String value)"
    },
    {
        "insertText": "addPermanentProxyRequestHeader(key, value)",
        "kind": 1,
        "label": "addPermanentProxyRequestHeader",
        "detail": "addPermanentProxyRequestHeader(String key, String value)"
    },
    {
        "insertText": "addPermanentProxyResponseHeader(key, value)",
        "kind": 1,
        "label": "addPermanentProxyResponseHeader",
        "detail": "addPermanentProxyResponseHeader(String key, String value)"
    },
    {
        "insertText": "getProxyUsername(exchange)",
        "kind": 1,
        "label": "getProxyUsername",
        "detail": "getProxyUsername(ServerWebExchange exchange)"
    },
    {
        "insertText": "getProxyPassword(exchange)",
        "kind": 1,
        "label": "getProxyPassword",
        "detail": "getProxyPassword(ServerWebExchange exchange)"
    },
    {
        "insertText": "getAppSecret(exchange)",
        "kind": 1,
        "label": "getAppSecret",
        "detail": "getAppSecret(ServerWebExchange exchange)"
    },
    {
        "insertText": "setAppSecret(exchange, secret)",
        "kind": 1,
        "label": "setAppSecret",
        "detail": "setAppSecret(ServerWebExchange exchange, String secret)"
    },
    {
        "insertText": "getAppBaseUrl()",
        "kind": 1,
        "label": "getAppBaseUrl",
        "detail": "getAppBaseUrl()"
    },
    {
        "insertText": "logInfo(text)",
        "kind": 1,
        "label": "logInfo",
        "detail": "logInfo(String text)"
    },
    {
        "insertText": "getRepositoryFacade()",
        "kind": 1,
        "label": "getRepositoryFacade",
        "detail": "getRepositoryFacade()"
    },
    {
        "insertText": "executeRequest(request)",
        "kind": 1,
        "label": "executeRequest",
        "detail": "executeRequest(HttpRequest request)"
    },
    {
        "insertText": "getBasicAuthenticationHeader(username, password)",
        "kind": 1,
        "label": "getBasicAuthenticationHeader",
        "detail": "getBasicAuthenticationHeader(String username, String password)"
    },
    {
        "insertText": "getJSONObjectFromString(body)",
        "kind": 1,
        "label": "getJSONObjectFromString",
        "detail": "getJSONObjectFromString(String body)"
    },
    {
        "insertText": "createGatewayFilter(closure)",
        "kind": 1,
        "label": "createGatewayFilter",
        "detail": "createGatewayFilter(Closure closure)"
    },
    {
        "insertText": "executeScript(exchange, scriptType)",
        "kind": 1,
        "label": "executeScript",
        "detail": "executeScript(ServerWebExchange exchange, ScriptType scriptType)"
    },
    {
        "insertText": "executeScript(exchange, scriptType)",
        "kind": 1,
        "label": "executeScript",
        "detail": "executeScript(ServerWebExchange exchange, String scriptType)"
    }
];