package dev.heinzl.simplessoproxy.repositories;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class CredentialsRepository {
    LoadingCache<String, String> credentials = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, String>() {
                        public String load(String key) {
                            return null;
                        }
                    });
}
