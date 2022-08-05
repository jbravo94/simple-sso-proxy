package dev.heinzl.simplessoproxy.scripting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;

public class ScriptingApiImplTests {

    @Test
    void testGetProxyUsername() {
        App app = App.builder().build();

        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

        GatewayFilterSpec gatewayFilterSpec = mock(GatewayFilterSpec.class);

        RepositoryFacade repositoryFacade = mock(RepositoryFacade.class);

        ScriptingApiImpl scriptingApiImpl = new ScriptingApiImpl(app, gatewayFilterSpec, repositoryFacade,
                jwtTokenProvider);

        when(jwtTokenProvider.getUsernameFromRequest(any())).thenReturn("username");
        ServerWebExchange exchange = mock(ServerWebExchange.class);

        assertEquals("username", scriptingApiImpl.getProxyUsername(exchange));
    }
}
