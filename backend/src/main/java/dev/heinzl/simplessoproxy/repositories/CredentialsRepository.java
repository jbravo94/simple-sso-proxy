package dev.heinzl.simplessoproxy.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.heinzl.simplessoproxy.models.Credential;

public interface CredentialsRepository extends CrudRepository<Credential, String> {
    List<Credential> findByAppId(String id);
}
