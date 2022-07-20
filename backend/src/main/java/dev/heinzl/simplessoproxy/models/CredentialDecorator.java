package dev.heinzl.simplessoproxy.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.heinzl.simplessoproxy.repositories.SecretsRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
        this.secretsRepository.setSecret(this.getId());
    }
}