package dev.heinzl.simplessoproxy.scripting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.script.ScriptException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;

import dev.heinzl.simplessoproxy.apps.App;

@SpringBootTest
public class ScriptingApiFactoryTests {

    @Autowired
    ScriptingApiFactory scriptingApiFactory;

    @Mock
    GatewayFilterSpec gatewayFilterSpec;

    @Test
    void testSuccessfulSetup() {
        App app = App.builder().build();

        scriptingApiFactory.createScriptingApiObject(app, gatewayFilterSpec);
    }

    @Test
    void testFailedSetup() {
        App app = App.builder().proxyScript("{NO-CODE}").build();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            scriptingApiFactory.createScriptingApiObject(app, gatewayFilterSpec);
        });

        assertEquals(ScriptException.class, exception.getCause().getClass());
    }

    @Test
    void testScriptInjection() {
        App app = App.builder().proxyScript("}; { throw new IllegalStateException(\"Should not occur!\")").build();

        assertThrows(IllegalArgumentException.class, () -> {
            scriptingApiFactory.createScriptingApiObject(app, gatewayFilterSpec);
        });
    }

}
