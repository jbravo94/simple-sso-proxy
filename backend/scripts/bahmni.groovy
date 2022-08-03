def gatewayFilter = { exchange ->

    scriptingApi.addProxyResponseCookieIfNotPreset(exchange, "bahmni.user", "%22" + scriptingApi.getProxyUsername(exchange) + "%22", "/")
    scriptingApi.addProxyResponseCookieIfNotPreset(exchange, "bahmni.user.location", "%7B%22name%22%3A%22General%20Ward%22%2C%22uuid%22%3A%22baf7bd38-d225-11e4-9c67-080027b662ec%22%7D", "/")

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
                    .header("Cookie", "JSESSIONID=" + reportingSessionId + "; reporting_session=" + reportingSessionId)
                    .build()

    scriptingApi.executeRequest(loginRequest)
}

scriptingApi.setScript(ScriptType.LOGIN, loginScript)
scriptingApi.createGatewayFilter(gatewayFilter)
