package dev.heinzl.simplessoproxy.secrets;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class ExpiringSecretsRepositoryImpl implements SecretsRepository {

    private final LoadingCache<String, String> secrets;

    public ExpiringSecretsRepositoryImpl(@Value("${secrets-cache-timeout}") Integer cacheTimeout) {
        secrets = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheTimeout, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<String, String>() {
                            public String load(String key) {
                                return "";
                            }
                        });
    }

    @Override
    public String getSecret(String identifier) {
        try {
            return this.secrets.get(identifier);
        } catch (ExecutionException e) {
            return "";
        }
    }

    @Override
    public void setSecret(String identifier, String value) {
        this.secrets.put(identifier, value);
    }

    @Override
    public void removeSecret(String identifier) {
        this.secrets.invalidate(identifier);
    }

    @Override
    public void removeAllSecrets() {
        this.secrets.invalidateAll();
    }
}
