def gatewayFilter = { exchange ->

    scriptingApi.logInfo(scriptingApi.executeScript(exchange, ScriptType.LOGIN))
    scriptingApi.addProxyResponseCookieIfNotPreset(exchange, "bahmni.user", "%22" + scriptingApi.getProxyUsername(exchange) + "%22", "/")
    scriptingApi.addProxyResponseCookieIfNotPreset(exchange, "bahmni.user.location", "%7B%22name%22%3A%22General%20Ward%22%2C%22uuid%22%3A%22baf7bd38-d225-11e4-9c67-080027b662ec%22%7D", "/")
}

scriptingApi.createGatewayFilter(gatewayFilter)

def loginScript = { exchange ->

    def password = scriptingApi.getProxyPassword(exchange)

    if (password == null || "".equals(password)) {
        return
    }

    def valueList = exchange.getRequest().getCookies().get("reporting_session")

    if (valueList == null || valueList.size() == 0) {
        return
    }

    def sessionId = valueList.get(0).getValue()

    def request = HttpRequest.newBuilder()
                    .uri(new URI("https://demo.mybahmni.org/openmrs/ws/rest/v1/session?v=custom:(uuid)")).GET()
                    .header("Authorization", scriptingApi.getBasicAuthenticationHeader(scriptingApi.getProxyUsername(exchange), scriptingApi.getProxyPassword(exchange)))
                    .header("Cookie", "JSESSIONID=" + sessionId + "; reporting_session=" + sessionId).build()

    scriptingApi.logInfo(scriptingApi.executeRequest(request))
}

scriptingApi.setScript(ScriptType.LOGIN, loginScript)
