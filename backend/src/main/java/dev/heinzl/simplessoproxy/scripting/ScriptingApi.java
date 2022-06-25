package dev.heinzl.simplessoproxy.scripting;

public interface ScriptingApi {
    void addProxyRequestHeaderIfNotPreset(String key, String value);

    void addProxyCookieIfNotPreset(String name, String value);

    String getUsername();

    String getPassword();

}
