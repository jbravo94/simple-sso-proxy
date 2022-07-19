package dev.heinzl.simplessoproxy.scripting;

public interface ScriptingApi {
    void addProxyRequestHeaderIfNotPreset(String key, String value);

    void addProxyResponseHeaderIfNotPreset(String key, String value);

    void addProxyRequestCookieIfNotPreset(String name, String value);

    void addProxyResponseCookieIfNotPreset(String name, String value, String path);

    String getProxyUsername();

    String getProxyPassword();

    String getAppUsername();

    String getAppPassword();

}
