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
