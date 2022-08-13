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

    scriptingApi.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "bahmni.user", "%22" + scriptingApi.getProxyUsername(exchange) + "%22", "/")
    scriptingApi.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "bahmni.user.location", "%7B%22name%22%3A%22General%20Ward%22%2C%22uuid%22%3A%22baf7bd38-d225-11e4-9c67-080027b662ec%22%7D", "/")

    scriptingApi.logInfo(scriptingApi.executeScript(exchange, ScriptType.LOGIN))
}

def loginScript = { exchange ->

    def baseUrl = scriptingApi.getAppBaseUrl()
    def username = scriptingApi.getProxyUsername(exchange)
    def password = scriptingApi.getProxyPassword(exchange)

    if (StringUtils.isEmpty(password)) {
        return
    }

    def reportingSessionIdList = exchange.getRequest().getCookies().get("reporting_session")

    if (CollectionUtils.isEmpty(reportingSessionIdList)) {
        return
    }

    def reportingSessionId = reportingSessionIdList.get(0).getValue()

    def jSessionIdList = exchange.getRequest().getCookies().get("JSESSIONID")

    if (CollectionUtils.isEmpty(jSessionIdList)) {
        return
    }

    def jSessionId = jSessionIdList.get(0).getValue()

    def deleteRequest = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/openmrs/ws/rest/v1/session?v=custom:(uuid)"))
                    .DELETE()
                    .header("Authorization", scriptingApi.getBasicAuthenticationHeader(username, password))
                    .build()

    scriptingApi.executeRequest(deleteRequest)

    def loginRequest = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/openmrs/ws/rest/v1/session?v=custom:(uuid)"))
                    .GET()
                    .header("Authorization", scriptingApi.getBasicAuthenticationHeader(username, password))
                    .header("Cookie", "JSESSIONID=" + jSessionId + "; reporting_session=" + reportingSessionId)
                    .build()

    scriptingApi.executeRequest(loginRequest)
}

scriptingApi.setScript(ScriptType.LOGIN, loginScript)
scriptingApi.createGatewayFilter(gatewayFilter)
