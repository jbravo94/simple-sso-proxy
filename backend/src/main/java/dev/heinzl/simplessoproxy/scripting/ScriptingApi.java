package dev.heinzl.simplessoproxy.scripting;

import org.slf4j.Logger;

import dev.heinzl.simplessoproxy.repositories.RepositoryFacade;
import groovy.lang.Closure;

public interface ScriptingApi {
    void addProxyRequestHeaderIfNotPreset(String key, String value);

    void addProxyResponseHeaderIfNotPreset(String key, String value);

    void addProxyRequestCookieIfNotPreset(String name, String value);

    void addProxyResponseCookieIfNotPreset(String name, String value, String path);

    RepositoryFacade getRepositoryFacade();

    void createGatewayFilter(Closure closure);

    String getProxyUsername();

    String getProxyPassword();

    String getAppUsername();

    String getAppPassword();

    Logger getLogger();

    void logInfo(String text);
}
