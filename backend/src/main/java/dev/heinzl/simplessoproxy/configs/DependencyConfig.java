package dev.heinzl.simplessoproxy.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.heinzl.simplessoproxy.credentials.CredentialsRepository;
import dev.heinzl.simplessoproxy.credentials.InMemoryCredentialsRepositoryDecorator;
import dev.heinzl.simplessoproxy.credentials.PersistentCredentialsRepositoryImpl;
import dev.heinzl.simplessoproxy.secrets.ExpiringSecretsRepositoryImpl;
import dev.heinzl.simplessoproxy.secrets.InMemorySecretsRepositoryImpl;
import dev.heinzl.simplessoproxy.secrets.SecretsRepository;

@Configuration
public class DependencyConfig {

    // Strategy Pattern

    @Bean
    public CredentialsRepository credentialsRepository(@Value("${persist-app-secrets}") boolean persistAppSecrets,
            PersistentCredentialsRepositoryImpl persistentCredentialsRepository,
            InMemorySecretsRepositoryImpl inMemorySecretsRepository) {

        if (persistAppSecrets) {
            return persistentCredentialsRepository;
        } else {
            return new InMemoryCredentialsRepositoryDecorator(persistentCredentialsRepository,
                    inMemorySecretsRepository);
        }
    }

    @Bean
    public SecretsRepository secretsRepository(ExpiringSecretsRepositoryImpl expiringSecretsRepository) {
        return expiringSecretsRepository;
    }
}
