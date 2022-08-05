package dev.heinzl.simplessoproxy.scripting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.json.JSONObject;
import org.codehaus.groovy.runtime.MethodClosure;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;
import dev.heinzl.simplessoproxy.secrets.SecretsRepository;

@ExtendWith(MockitoExtension.class)
// FIX This
@MockitoSettings(strictness = Strictness.LENIENT)
public class ScriptingApiImplTests {

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    GatewayFilterSpec gatewayFilterSpec;

    @Mock
    RepositoryFacade repositoryFacade;

    @Mock
    ServerWebExchange exchange;

    ScriptingApiImpl scriptingApiImpl;
    App app;

    @BeforeEach
    void prepareBeforeEach() {
        app = App.builder().build();
        scriptingApiImpl = new ScriptingApiImpl(app, gatewayFilterSpec, repositoryFacade,
                jwtTokenProvider);
    }

    @Test
    void testGetProxyUsername() {
        when(jwtTokenProvider.getUsernameFromRequest(any())).thenReturn("username");

        assertEquals("username", scriptingApiImpl.getProxyUsername(exchange));
    }

    void prepareRequest() {
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        when(exchange.getRequest()).thenReturn(request);

        HttpHeaders requestHeaders = new HttpHeaders();
        when(request.getHeaders()).thenReturn(requestHeaders);

        MultiValueMap<String, HttpCookie> cookies = new LinkedMultiValueMap<>();
        when(request.getCookies()).thenReturn(cookies);
    }

    void prepareResponse() {

        ServerHttpResponse response = mock(ServerHttpResponse.class);
        when(exchange.getResponse()).thenReturn(response);

        HttpHeaders responseHeaders = new HttpHeaders();
        when(response.getHeaders()).thenReturn(responseHeaders);

        MultiValueMap<String, ResponseCookie> cookies = new LinkedMultiValueMap<>();
        when(response.getCookies()).thenReturn(cookies);
    }

    @Test
    void positiveTestAddProxyRequestHeaderIfNotPreset() {

        prepareRequest();

        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "key", "value");

        assertTrue(exchange.getRequest().getHeaders().get("key").contains("value"));
    }

    @Test
    void negativeTestAddProxyRequestHeaderIfNotPreset() {

        prepareRequest();

        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "key", "value_1");
        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "key", "value_2");

        assertTrue(exchange.getRequest().getHeaders().get("key").contains("value_1"));
        assertFalse(exchange.getRequest().getHeaders().get("key").contains("value_2"));
    }

    @Test
    void positiveTestAddProxyResponseHeaderIfNotPreset() {

        prepareResponse();

        scriptingApiImpl.addProxyResponseHeaderIfNotPreset(exchange, "key", "value");

        assertTrue(exchange.getResponse().getHeaders().get("key").contains("value"));
    }

    @Test
    void negativeTestAddProxyResponseHeaderIfNotPreset() {

        prepareResponse();

        scriptingApiImpl.addProxyResponseHeaderIfNotPreset(exchange, "key", "value_1");
        scriptingApiImpl.addProxyResponseHeaderIfNotPreset(exchange, "key", "value_2");

        assertTrue(exchange.getResponse().getHeaders().get("key").contains("value_1"));
        assertFalse(exchange.getResponse().getHeaders().get("key").contains("value_2"));
    }

    @Test
    void positiveTestAddProxyRequestCookieIfNotPreset() {
        prepareRequest();

        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "KEY", "value");

        assertTrue(exchange.getRequest().getCookies().get("key").stream()
                .anyMatch(cookie -> "value".equals(cookie.getValue())));

    }

    @Test
    void negativeTestAddProxyRequestCookieIfNotPreset() {
        prepareRequest();

        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "key", "value_1");
        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "key", "value_2");

        assertTrue(exchange.getRequest().getCookies().get("key").stream()
                .anyMatch(cookie -> "value_1".equals(cookie.getValue())));
        assertFalse(exchange.getRequest().getCookies().get("key").stream()
                .anyMatch(cookie -> "value_2".equals(cookie.getValue())));
    }

    @Test
    void positiveTestAddProxyResponseSetCookieIfNotPresentInRequest() {
        prepareRequest();
        prepareResponse();

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value", "/");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "key=value; Path=/".equals(header)));
    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequest() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("key", new HttpCookie("key", "value_1"));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value_2");

        assertNull(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE));

    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequestWithOtherSetCookieHeader() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("key", new HttpCookie("key", "value_1"));
        exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE, "key=value_x; Path=/");

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value_2");

        assertFalse(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "key=value_2; Path=/".equals(header)));
    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequestWithNullCookieValue() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("key", new HttpCookie("key", null));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "key=value; Path=/".equals(header)));
    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequestWithNullStringCookieValue() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("key", new HttpCookie("key", "null"));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "key=value; Path=/".equals(header)));
    }

    @Test
    void testAddProxyResponseSetCookieIfNotPresentInRequestWithNullPath() {
        assertThrows(NullPointerException.class, () -> {
            scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value", null);
        });
    }

    @Test
    void testGetJSONObjectFromString() {
        JSONObject jsonObject = scriptingApiImpl.getJSONObjectFromString("{\"key\":\"value\"}");
        assertEquals("value", jsonObject.getString("key"));
    }

    @Test
    void testAddPermanentProxyRequestHeader() {
        scriptingApiImpl.addPermanentProxyRequestHeader("key", "value");
        verify(gatewayFilterSpec, times(1)).addRequestHeader("key", "value");
    }

    @Test
    void testAddPermanentProxyResponseHeader() {
        scriptingApiImpl.addPermanentProxyResponseHeader("key", "value");
        verify(gatewayFilterSpec, times(1)).addResponseHeader("key", "value");
    }

    @Test
    void testGetProxyPassword() {
        when(jwtTokenProvider.getUsernameFromRequest(exchange.getRequest())).thenReturn("username");
        when(repositoryFacade.getSecretsRepository()).thenReturn(mock(SecretsRepository.class));
        when(repositoryFacade.getSecretsRepository().getSecret("username")).thenReturn("password");

        String password = scriptingApiImpl.getProxyPassword(exchange);

        assertEquals("password", password);
    }

    @Test
    void testGetLogger() {
        Logger logger = scriptingApiImpl.getLogger();

        assertNotNull(logger);
    }

    @Test
    void testLogInfo() {
        scriptingApiImpl.logInfo("text");
    }

    @Test
    void testGetRepositoryFacade() {
        RepositoryFacade getRepositoryFacade = scriptingApiImpl.getRepositoryFacade();

        assert repositoryFacade == getRepositoryFacade;
    }

    @Test
    void positiveTestSetScript() {
        scriptingApiImpl.setScript("LOGIN", new MethodClosure(this, "none"));
    }

    @Test
    void negativeTestSetScript() {
        fail();
    }

}
