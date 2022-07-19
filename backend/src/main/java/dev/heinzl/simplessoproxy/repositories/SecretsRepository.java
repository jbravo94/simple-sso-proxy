package dev.heinzl.simplessoproxy.repositories;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class SecretsRepository {
    private final LoadingCache<String, String> secrets = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, String>() {
                        public String load(String key) {
                            return null;
                        }
                    });

    public String getSecret(String identifier) {
        try {
            return this.secrets.get(identifier);
        } catch (ExecutionException e) {
            return null;
        }
    }

    public void putSecret(String identifier) {
        this.putSecret(identifier);
    }
}
