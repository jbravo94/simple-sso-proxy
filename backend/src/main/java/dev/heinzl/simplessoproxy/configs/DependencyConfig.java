package dev.heinzl.simplessoproxy.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.heinzl.simplessoproxy.repositories.CredentialsRepository;
import dev.heinzl.simplessoproxy.repositories.PersistentCredentialsRepository;

@Configuration
public class DependencyConfig {

    // Strategy Pattern

    @Bean
    public CredentialsRepository credentialsRepository(@Value("${persist-app-secrets}") boolean persistAppSecrets,
            PersistentCredentialsRepository persistentCredentialsRepository) {

        if (persistAppSecrets) {
            return persistentCredentialsRepository;
        } else {
            return null;
        }
    }
}
