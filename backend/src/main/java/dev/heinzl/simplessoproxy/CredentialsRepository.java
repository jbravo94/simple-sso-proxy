package dev.heinzl.simplessoproxy;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CredentialsRepository extends MongoRepository<Credential, String> {
    List<Credential> findByName(String name);
}