package dev.heinzl.simplessoproxy.repositories;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemorySecretsRepository implements SecretsRepository {

    private final Map<String, String> secrets = new HashMap<>();

    @Override
    public String getSecret(String identifier) {
        return this.secrets.get(identifier);
    }

    @Override
    public void setSecret(String identifier, String value) {
        this.secrets.put(identifier, value);
    }

    @Override
    public void removeSecret(String identifier) {
        this.secrets.remove(identifier);
    }

    public void removeAllSecrets() {
        this.secrets.clear();
    }
}
