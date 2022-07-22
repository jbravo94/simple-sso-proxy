package dev.heinzl.simplessoproxy.repositories;

public interface SecretsRepository {

    String getSecret(String identifier);

    void setSecret(String identifier, String value);

    void removeSecret(String identifier);

    void removeAllSecrets();
}
