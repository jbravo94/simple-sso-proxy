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
def gatewayFilter = { exchange ->

    def baseUrl = scriptingApi.getAppBaseUrl()

    scriptingApi.logInfo(scriptingApi.executeScript(exchange, ScriptType.LOGIN))

    if ("/web/login".equals(exchange.getRequest().getPath().toString())) {

        exchange.getResponse().getHeaders().add("Location", "/web")
        exchange.getResponse().setRawStatusCode(303)
    }

    def locationHeader = exchange.getResponse().getHeaders().get("Location")

    if (locationHeader != null && locationHeader.size() > 0 && ("http://erp.health.local/web/login").equals(locationHeader.get(0))) {
        exchange.getResponse().getHeaders().set("Location", "/web/login") 
    }

}

def loginScript = { exchange ->

    def baseUrl = scriptingApi.getAppBaseUrl()
    def username = scriptingApi.getProxyUsername(exchange)
    def password = scriptingApi.getProxyPassword(exchange)

    if (StringUtils.isEmpty(password)) {
        return
    }

    def valueList = exchange.getRequest().getCookies().get("session_id")

    if (CollectionUtils.isEmpty(valueList)) {
        return
    }

    def sessionId = valueList.get(0).getValue()

    def body = "{\"jsonrpc\":\"2.0\",\"params\":{\"db\":\"odoo\",\"login\":\"" + username + "\",\"password\":\"" + password + "\"}}"

    def request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/web/session/authenticate"))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .header("Cookie", "session_id=" + sessionId)
                    .build()

    scriptingApi.executeRequest(request)
}

scriptingApi.setScript(ScriptType.LOGIN, loginScript)
scriptingApi.createGatewayFilter(gatewayFilter)
