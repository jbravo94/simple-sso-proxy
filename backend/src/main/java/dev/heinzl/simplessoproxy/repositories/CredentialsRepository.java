package dev.heinzl.simplessoproxy.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import dev.heinzl.simplessoproxy.models.Credential;

@NoRepositoryBean
public interface CredentialsRepository extends CrudRepository<Credential, String> {
    List<Credential> findByAppId(String id);
}
