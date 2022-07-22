package dev.heinzl.simplessoproxy.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import dev.heinzl.simplessoproxy.models.Credential;

@NoRepositoryBean
public interface DefaultCredentialsRepository extends CredentialsRepository, MongoRepository<Credential, String> {
    @Query(value = "{ 'app._id' : ObjectId(?0) }")
    public List<Credential> findByAppId(String id);
}