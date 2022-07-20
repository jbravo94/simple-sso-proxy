package dev.heinzl.simplessoproxy.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.heinzl.simplessoproxy.repositories.CredentialsRepository;
import dev.heinzl.simplessoproxy.repositories.PersistentCredentialsRepository;

//@Configuration
public class DependencyConfig {

    @Bean
    public CredentialsRepository credentialsRepository(
            PersistentCredentialsRepository persistentCredentialsRepository) {
        return persistentCredentialsRepository;
    }
}
