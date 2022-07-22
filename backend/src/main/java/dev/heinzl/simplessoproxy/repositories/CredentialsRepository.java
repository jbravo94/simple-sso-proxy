package dev.heinzl.simplessoproxy.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.heinzl.simplessoproxy.models.Credential;

public interface CredentialsRepository extends MongoRepository<Credential, String> {

}