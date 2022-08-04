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
