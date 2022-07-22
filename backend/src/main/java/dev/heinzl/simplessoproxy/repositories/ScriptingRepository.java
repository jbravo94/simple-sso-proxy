package dev.heinzl.simplessoproxy.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;

@Component
public class ScriptingRepository {
    private final Map<String, ScriptingApi> scriptingRepository = new HashMap<>();

    public void cacheScriptForAppProxyUrl(String proxyUrl, ScriptingApi scriptingApi) {
        this.scriptingRepository.put(proxyUrl, scriptingApi);
    }

    public ScriptingApi getScriptingApiForAppProxyUrl(String proxyUrl) {
        return this.scriptingRepository.get(proxyUrl);
    }
}
