package dev.heinzl.simplessoproxy.models;

import dev.heinzl.simplessoproxy.repositories.SecretsRepository;

public class CredentialDecorator extends Credential {

    private SecretsRepository secretsRepository;

    public CredentialDecorator(String id, User user, App name, String secret) {
        super(id, user, name, secret);
    }

    public static Credential from(Credential credential) {
        CredentialDecorator credentialDecorator = new CredentialDecorator(
                credential.getId(),
                credential.getUser(),
                credential.getApp(),
                credential.getSecret());
        return credentialDecorator;
    }

    @Override
    public String getSecret() {
        return secretsRepository.getSecret(this.getId());
    }

    @Override
    public void setSecret(String secret) {
        // TODO Fix if empty string
        this.secretsRepository.setSecret(this.getId(), secret);
    }
}