package dev.heinzl.simplessoproxy.scripting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;

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

        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "KEY", "VALUE");

        assertTrue(exchange.getRequest().getHeaders().get("KEY").contains("VALUE"));
    }

    @Test
    void negativeTestAddProxyRequestHeaderIfNotPreset() {

        prepareRequest();

        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "KEY", "VALUE_1");
        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "KEY", "VALUE_2");

        assertTrue(exchange.getRequest().getHeaders().get("KEY").contains("VALUE_1"));
        assertFalse(exchange.getRequest().getHeaders().get("KEY").contains("VALUE_2"));
    }

    @Test
    void positiveTestAddProxyRequestCookieIfNotPreset() {
        prepareRequest();

        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "KEY", "VALUE");

        assertTrue(exchange.getRequest().getCookies().get("KEY").stream()
                .anyMatch(cookie -> "VALUE".equals(cookie.getValue())));

    }

    @Test
    void negativeTestAddProxyRequestCookieIfNotPreset() {
        prepareRequest();

        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "KEY", "VALUE_1");
        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "KEY", "VALUE_2");

        assertTrue(exchange.getRequest().getCookies().get("KEY").stream()
                .anyMatch(cookie -> "VALUE_1".equals(cookie.getValue())));
        assertFalse(exchange.getRequest().getCookies().get("KEY").stream()
                .anyMatch(cookie -> "VALUE_2".equals(cookie.getValue())));
    }

    @Test
    void positiveTestAddProxyResponseSetCookieIfNotPresentInRequest() {
        prepareRequest();
        prepareResponse();

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "KEY", "VALUE", "/");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "KEY=VALUE; Path=/".equals(header)));
    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequest() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("KEY", new HttpCookie("KEY", "VALUE_1"));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "KEY", "VALUE_2");

        assertNull(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE));

    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequestWithOtherSetCookieHeader() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("KEY", new HttpCookie("KEY", "VALUE_1"));
        exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE, "KEY=VALUE_X; Path=/");

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "KEY", "VALUE_2");

        assertFalse(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "KEY=VALUE_2; Path=/".equals(header)));
    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequestWithNullCookieValue() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("KEY", new HttpCookie("KEY", null));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "KEY", "VALUE");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "KEY=VALUE; Path=/".equals(header)));
    }

    @Test
    void negativeTestAddProxyResponseSetCookieIfNotPresentInRequestWithNullStringCookieValue() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("KEY", new HttpCookie("KEY", "null"));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "KEY", "VALUE");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "KEY=VALUE; Path=/".equals(header)));
    }

    @Test
    void testAddProxyResponseSetCookieIfNotPresentInRequestWithNullPath() {
        assertThrows(NullPointerException.class, () -> {
            scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "KEY", "VALUE", null);
        });
    }

}
