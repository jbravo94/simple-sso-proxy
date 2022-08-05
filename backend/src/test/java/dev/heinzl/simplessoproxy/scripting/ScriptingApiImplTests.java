package dev.heinzl.simplessoproxy.scripting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
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
    }

    void prepareResponse() {
        ServerHttpResponse response = mock(ServerHttpResponse.class);
        when(exchange.getResponse()).thenReturn(response);

        HttpHeaders responseHeaders = new HttpHeaders();
        when(response.getHeaders()).thenReturn(responseHeaders);
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
}
