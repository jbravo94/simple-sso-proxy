package dev.heinzl.simplessoproxy.models;

import dev.heinzl.simplessoproxy.repositories.SecretsRepository;
import lombok.NonNull;

public class CredentialDecorator extends Credential {

    private SecretsRepository secretsRepository;

    private CredentialDecorator(String id, User user, App name, String secret, SecretsRepository secretsRepository) {
        super(id, user, name, null);
        this.setSecret(secret);
        this.secretsRepository = secretsRepository;
    }

    public static Credential from(Credential credential, SecretsRepository secretsRepository) {

        CredentialDecorator credentialDecorator = new CredentialDecorator(
                credential.getId(),
                credential.getUser(),
                credential.getApp(),
                credential.getSecret(),
                secretsRepository);
        return credentialDecorator;
    }

    public Credential toCredential() {
        return new Credential(this.getId(), this.getUser(), this.getApp(), this.getSecret());
    }

    public Credential toCredentialWithoutSecret() {
        return new Credential(this.getId(), this.getUser(), this.getApp(), null);
    }

    @Override
    public String getSecret() {
        return secretsRepository.getSecret(this.getId());
    }

    @Override
    public void setSecret(@NonNull String secret) {
        this.secretsRepository.setSecret(this.getId(), secret);
    }
}