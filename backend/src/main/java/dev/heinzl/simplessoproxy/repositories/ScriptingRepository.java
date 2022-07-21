package dev.heinzl.simplessoproxy.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import dev.heinzl.simplessoproxy.scripting.ScriptingApi;

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
