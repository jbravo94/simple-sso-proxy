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
package dev.heinzl.simplessoproxy.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.heinzl.simplessoproxy.credentials.CredentialsRepository;
import dev.heinzl.simplessoproxy.credentials.CredentialsRepositoryStrategy;
import dev.heinzl.simplessoproxy.credentials.InMemoryCredentialsRepositoryDecorator;
import dev.heinzl.simplessoproxy.credentials.PersistentCredentialsRepositoryImpl;
import dev.heinzl.simplessoproxy.secrets.ExpiringSecretsRepositoryImpl;
import dev.heinzl.simplessoproxy.secrets.InMemorySecretsRepositoryImpl;
import dev.heinzl.simplessoproxy.secrets.SecretsRepository;

@Configuration
public class DependencyConfig {

    @Bean
    public CredentialsRepository credentialsRepository(
            @Value("${credentials-repository-strategy}") String credentialsRepositoryStrategy,
            PersistentCredentialsRepositoryImpl persistentCredentialsRepository,
            InMemorySecretsRepositoryImpl inMemorySecretsRepository) {

        CredentialsRepositoryStrategy strategy = CredentialsRepositoryStrategy.valueOf(credentialsRepositoryStrategy);

        switch (strategy) {
            case PERSISTENT_SECRETS:
                return persistentCredentialsRepository;
            case INMEMORY_SECRETS:
                return new InMemoryCredentialsRepositoryDecorator(persistentCredentialsRepository,
                        inMemorySecretsRepository);
            default:
                throw new IllegalStateException(
                        "credentials-respository-strategy property must contain either PERSISTENT_SECRETS or INMEMORY_SECRETS value!");
        }

    }

    @Bean
    public SecretsRepository secretsRepository(ExpiringSecretsRepositoryImpl expiringSecretsRepository) {
        return expiringSecretsRepository;
    }
}
