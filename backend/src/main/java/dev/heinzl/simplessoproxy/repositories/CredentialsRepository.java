package dev.heinzl.simplessoproxy.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.heinzl.simplessoproxy.models.Credential;

public interface CredentialsRepository extends MongoRepository<Credential, String> {
    List<Credential> findByName(String name);
}