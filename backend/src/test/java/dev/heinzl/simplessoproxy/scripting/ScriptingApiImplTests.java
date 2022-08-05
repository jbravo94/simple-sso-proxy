package dev.heinzl.simplessoproxy.scripting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.collect.Lists;

import org.json.JSONObject;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.assertj.core.util.Arrays;
import org.codehaus.groovy.runtime.MethodClosure;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import dev.heinzl.simplessoproxy.apps.App;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;
import dev.heinzl.simplessoproxy.credentials.Credential;
import dev.heinzl.simplessoproxy.credentials.CredentialsRepository;
import dev.heinzl.simplessoproxy.scripting.api.ScriptType;
import dev.heinzl.simplessoproxy.secrets.SecretsRepository;
import dev.heinzl.simplessoproxy.users.User;
import dev.heinzl.simplessoproxy.users.UsersRepository;
import groovy.lang.Closure;

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
    void testGetProxyUsername() {
        when(jwtTokenProvider.getUsernameFromRequest(any())).thenReturn("username");

        assertEquals("username", scriptingApiImpl.getProxyUsername(exchange));
    }

    @Test
    void testAddProxyRequestHeaderIfNotPreset() {

        prepareRequest();

        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "key", "value");

        assertTrue(exchange.getRequest().getHeaders().get("key").contains("value"));
    }

    @Test
    void testAddProxyRequestHeaderIfNotPresetForDuplicateHandling() {

        prepareRequest();

        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "key", "value_1");
        scriptingApiImpl.addProxyRequestHeaderIfNotPreset(exchange, "key", "value_2");

        assertTrue(exchange.getRequest().getHeaders().get("key").contains("value_1"));
        assertFalse(exchange.getRequest().getHeaders().get("key").contains("value_2"));
    }

    @Test
    void testAddProxyResponseHeaderIfNotPreset() {

        prepareResponse();

        scriptingApiImpl.addProxyResponseHeaderIfNotPreset(exchange, "key", "value");

        assertTrue(exchange.getResponse().getHeaders().get("key").contains("value"));
    }

    @Test
    void testAddProxyResponseHeaderIfNotPresetForDuplicateHandling() {

        prepareResponse();

        scriptingApiImpl.addProxyResponseHeaderIfNotPreset(exchange, "key", "value_1");
        scriptingApiImpl.addProxyResponseHeaderIfNotPreset(exchange, "key", "value_2");

        assertTrue(exchange.getResponse().getHeaders().get("key").contains("value_1"));
        assertFalse(exchange.getResponse().getHeaders().get("key").contains("value_2"));
    }

    @Test
    void testAddProxyRequestCookieIfNotPreset() {
        prepareRequest();

        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "key", "value");

        assertTrue(exchange.getRequest().getCookies().get("key").stream()
                .anyMatch(cookie -> "value".equals(cookie.getValue())));

    }

    @Test
    void testAddProxyRequestCookieIfNotPresetForDuplicateHandling() {
        prepareRequest();

        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "key", "value_1");
        scriptingApiImpl.addProxyRequestCookieIfNotPreset(exchange, "key", "value_2");

        assertTrue(exchange.getRequest().getCookies().get("key").stream()
                .anyMatch(cookie -> "value_1".equals(cookie.getValue())));
        assertFalse(exchange.getRequest().getCookies().get("key").stream()
                .anyMatch(cookie -> "value_2".equals(cookie.getValue())));
    }

    @Test
    void testAddProxyResponseSetCookieIfNotPresentInRequest() {
        prepareRequest();
        prepareResponse();

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value", "/");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "key=value; Path=/".equals(header)));
    }

    @Test
    void testAddProxyResponseSetCookieIfNotPresentInRequestForDuplicateHandling() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("key", new HttpCookie("key", "value_1"));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value_2");

        assertNull(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE));

    }

    @Test
    void testAddProxyResponseSetCookieIfNotPresentInRequestWithOtherSetCookieHeader() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("key", new HttpCookie("key", "value_1"));
        exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE, "key=value_x; Path=/");

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value_2");

        assertFalse(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "key=value_2; Path=/".equals(header)));
    }

    @Test
    void testAddProxyResponseSetCookieIfNotPresentInRequestWithNullCookieValue() {
        prepareRequest();
        prepareResponse();

        exchange.getRequest().getCookies().add("key", new HttpCookie("key", null));

        scriptingApiImpl.addProxyResponseSetCookieIfNotPresentInRequest(exchange, "key", "value");

        assertTrue(exchange.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE).stream()
                .anyMatch(header -> "key=value; Path=/".equals(header)));
    }

    @Test
    void testAddProxyResponseSetCookieIfNotPresentInRequestWithNullStringCookieValue() {
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
    void testSetScript() {
        scriptingApiImpl.setScript("ANY", new MethodClosure(this, "none"));
    }

    @Test
    void testSetScriptWithType() {
        scriptingApiImpl.setScript(ScriptType.LOGIN, new MethodClosure(this, "none"));
    }

    @Test
    void testGetBasicAuthenticationHeader() {
        String basicEncodedCredentials = scriptingApiImpl.getBasicAuthenticationHeader("username", "password");
        assertEquals("Basic dXNlcm5hbWU6cGFzc3dvcmQ=", basicEncodedCredentials);
    }

    @Test
    void testGetBasicAuthenticationHeaderWithVaryingNulledParameter() {
        assertThrows(NullPointerException.class, () -> {
            scriptingApiImpl.getBasicAuthenticationHeader(null, "password");
        });
        assertThrows(NullPointerException.class, () -> {
            scriptingApiImpl.getBasicAuthenticationHeader("username", null);
        });
        assertThrows(NullPointerException.class, () -> {
            scriptingApiImpl.getBasicAuthenticationHeader(null, null);
        });
    }

    @Test
    void testExecuteScript() {
        Closure closure = mock(Closure.class);

        scriptingApiImpl.setScript(ScriptType.LOGIN, closure);

        scriptingApiImpl.executeScript(exchange, ScriptType.LOGIN);

        verify(closure, times(1)).call(exchange);
    }

    @Test
    void testExecuteScriptWithNotLoadedScript() {
        assertThrows(IllegalArgumentException.class, () -> {
            scriptingApiImpl.executeScript(exchange, ScriptType.LOGIN);
        });
    }

    @Test
    void testGetAppBaseUrl() {
        String url = "http://example.com";

        app.setBaseUrl(url);

        String appBaseUrl = scriptingApiImpl.getAppBaseUrl();

        assertEquals(url, appBaseUrl);
    }

    @Test
    void testCreateGatewayFilter() {
        prepareRequest();
        prepareResponse();

        Closure closure = mock(Closure.class);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        ArgumentCaptor<GatewayFilter> argument = ArgumentCaptor.forClass(GatewayFilter.class);

        scriptingApiImpl.createGatewayFilter(closure);

        verify(gatewayFilterSpec, times(1)).filter(argument.capture());

        GatewayFilter gatewayFilter = argument.getValue();
        gatewayFilter.filter(exchange, chain);

        verify(closure, times(1)).call(exchange);
        verify(chain, times(1)).filter(exchange);
    }

    @Test
    void testExecuteRequest() {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://example.com")).GET().build();

        HttpResponse<String> response = scriptingApiImpl.executeRequest(request);

        assertEquals(200, response.statusCode());
        assertFalse(StringUtils.isEmpty(response.body()));
        assertTrue(response.headers().firstValue("content-type").isPresent());
    }

    @Test
    void testExecuteRequestExpectFailure() {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://nothing.nothing/nothing")).GET().build();

        assertThrows(IllegalStateException.class, () -> {
            scriptingApiImpl.executeRequest(request);
        });
    }

    @Test
    void testSetAppSecret() {

        ScriptingApiImpl spiedScriptingApiImpl = spy(scriptingApiImpl);

        User user = User.builder().username("username").build();
        Credential credential = Credential.builder().build();

        doReturn("username").when(spiedScriptingApiImpl).getProxyUsername(exchange);
        doReturn(user).when(spiedScriptingApiImpl).getProxyUser("username");
        doReturn(credential).when(spiedScriptingApiImpl).getAppCredential(user);
        doReturn(credential).when(spiedScriptingApiImpl).updateAppCredential(credential, user, "secret");

        spiedScriptingApiImpl.setAppSecret(exchange, "secret");

        verify(spiedScriptingApiImpl, times(1)).updateAppCredential(credential, user, "secret");
    }

    @Test
    void testGetProxyUser() {
        User originalUser = User.builder().username("username").build();
        UsersRepository usersRepository = mock(UsersRepository.class);
        when(usersRepository.findByUsername("username")).thenReturn(originalUser);
        when(repositoryFacade.getUsersRepository()).thenReturn(usersRepository);

        User user = scriptingApiImpl.getProxyUser("username");

        assert originalUser == user;
    }

    @Test
    void testUpdateAppCredential() {
        User user = User.builder().build();
        Credential originalCredential = Credential.builder().app(app).user(user).secret("oldsecret").build();

        CredentialsRepository credentialRepository = mock(CredentialsRepository.class);

        when(credentialRepository.save(originalCredential)).thenReturn(originalCredential);
        when(repositoryFacade.getCredentialsRepository()).thenReturn(credentialRepository);

        Credential credential = scriptingApiImpl.updateAppCredential(originalCredential, user, "secret");

        assert app == credential.getApp();
        assert user == credential.getUser();
        assert "secret" == credential.getSecret();
    }

    @Test
    void testUpdateAppCredentialIfCredentialIsNull() {
        User user = User.builder().build();

        CredentialsRepository credentialRepository = mock(CredentialsRepository.class);

        when(credentialRepository.save(any())).thenAnswer(returnsFirstArg());
        when(repositoryFacade.getCredentialsRepository()).thenReturn(credentialRepository);

        Credential credential = scriptingApiImpl.updateAppCredential(null, user, "secret");

        assert app == credential.getApp();
        assert user == credential.getUser();
        assert "secret" == credential.getSecret();
    }

    @Test
    void testGetAppSecret() {
        ScriptingApiImpl spiedScriptingApiImpl = spy(scriptingApiImpl);

        Credential credential = Credential.builder().secret("secret").build();
        doReturn(credential).when(spiedScriptingApiImpl).getAppCredential(exchange);

        String secret = spiedScriptingApiImpl.getAppSecret(exchange);

        assertEquals("secret", secret);
    }

    @Test
    void testGetAppSecretIfCredentialIsNull() {
        ScriptingApiImpl spiedScriptingApiImpl = spy(scriptingApiImpl);

        doReturn(null).when(spiedScriptingApiImpl).getAppCredential(exchange);

        String secret = spiedScriptingApiImpl.getAppSecret(exchange);

        assertNull(secret);
    }

    @Test
    void testGetAppCredential() {

        ScriptingApiImpl spiedScriptingApiImpl = spy(scriptingApiImpl);

        Credential credential = Credential.builder().build();
        User user = User.builder().username("username").build();

        doReturn("username").when(spiedScriptingApiImpl).getProxyUsername(exchange);
        doReturn(user).when(spiedScriptingApiImpl).getProxyUser("username");
        doReturn(credential).when(spiedScriptingApiImpl).getAppCredential(user);

        assert credential == spiedScriptingApiImpl.getAppCredential(exchange);
    }

    @Test
    void testGetAppCredentialForUser() {

        User user = User.builder().build();
        Credential originalCredential = Credential.builder().secret("secret").build();
        CredentialsRepository credentialRepository = mock(CredentialsRepository.class);

        when(credentialRepository.findByAppIdAndUserId(app.getId(), user.getId()))
                .thenReturn(Lists.newArrayList(originalCredential));

        when(repositoryFacade.getCredentialsRepository()).thenReturn(credentialRepository);

        Credential credential = scriptingApiImpl.getAppCredential(user);

        assertEquals(originalCredential.getSecret(), credential.getSecret());
    }

    @Test
    void testGetAppCredentialForUserIfNoneSaved() {
        User user = User.builder().build();
        CredentialsRepository credentialRepository = mock(CredentialsRepository.class);

        when(credentialRepository.findByAppIdAndUserId(app.getId(), user.getId()))
                .thenReturn(Lists.newArrayList());

        when(repositoryFacade.getCredentialsRepository()).thenReturn(credentialRepository);

        Credential credential = scriptingApiImpl.getAppCredential(user);

        assertNull(credential);
    }

    @Test
    void testGetAppCredentialForUserIfMultipleCredentialsFound() {
        User user = User.builder().build();
        Credential credential = Credential.builder().secret("secret").build();
        CredentialsRepository credentialRepository = mock(CredentialsRepository.class);

        when(credentialRepository.findByAppIdAndUserId(app.getId(), user.getId()))
                .thenReturn(Lists.newArrayList(credential, credential));

        when(repositoryFacade.getCredentialsRepository()).thenReturn(credentialRepository);

        assertThrows(IllegalStateException.class, () -> {
            scriptingApiImpl.getAppCredential(user);
        });
    }
}
