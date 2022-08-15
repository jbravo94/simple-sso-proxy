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
