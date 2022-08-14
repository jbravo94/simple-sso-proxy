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
package dev.heinzl.simplessoproxy.scripting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.time.Duration;

import javax.script.ScriptException;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import dev.heinzl.simplessoproxy.apps.App;

@ActiveProfiles("test")
@SpringBootTest
public class ScriptingApiFactoryTests {

    @Autowired
    ScriptingApiFactory scriptingApiFactory;

    @Mock
    GatewayFilterSpec gatewayFilterSpec;

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(
            new File("integration-testing/database/mongodb-docker-compose.yml"))
            .withExposedService("mongo_1", 27017,
                    Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(15)))
            .withLocalCompose(true);

    @BeforeAll
    public static void prepare() {
        environment.start();
    }

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
