def gatewayFilter = { exchange ->

    scriptingApi.logInfo(scriptingApi.executeScript(exchange, ScriptType.LOGIN))
}

scriptingApi.createGatewayFilter(gatewayFilter)

def loginScript = { exchange ->

    def password = scriptingApi.getProxyPassword(exchange)

    if (password == null || "".equals(password)) {
        return
    }

    def valueList = exchange.getRequest().getCookies().get("session_id")

    if (valueList == null || valueList.size() == 0) {
        return
    }

    def sessionId = valueList.get(0).getValue()

    def body = "{\"jsonrpc\":\"2.0\",\"params\":{\"db\":\"odoo\",\"login\":\"" + scriptingApi.getProxyUsername(exchange) + "\",\"password\":\"" + scriptingApi.getProxyPassword(exchange) + "\"}}"

    def request = HttpRequest.newBuilder()
                    .uri(new URI("https://erp.v-suite.healthcare/web/session/authenticate")).POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .header("Cookie", "session_id=" + sessionId).build()

    scriptingApi.logInfo(scriptingApi.executeRequest(request))
}

scriptingApi.setScript(ScriptType.LOGIN, loginScript)
