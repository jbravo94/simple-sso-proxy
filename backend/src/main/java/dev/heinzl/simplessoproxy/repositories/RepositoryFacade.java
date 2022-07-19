package dev.heinzl.simplessoproxy.repositories;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@Getter
@AllArgsConstructor
public class RepositoryFacade {
    private final UsersRepository usersRepository;
    private final AppsRepository appsRepository;
    private final CredentialsRepository credentialsRepository;
    private final SecretsRepository secretsRepository;
}
