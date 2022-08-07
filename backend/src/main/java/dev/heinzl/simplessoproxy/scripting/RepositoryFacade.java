package dev.heinzl.simplessoproxy.scripting;

import org.springframework.stereotype.Service;

import dev.heinzl.simplessoproxy.apps.AppsRepository;
import dev.heinzl.simplessoproxy.credentials.CredentialsRepository;
import dev.heinzl.simplessoproxy.secrets.SecretsRepository;
import dev.heinzl.simplessoproxy.users.UsersRepository;
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
