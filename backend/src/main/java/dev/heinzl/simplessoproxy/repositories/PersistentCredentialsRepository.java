package dev.heinzl.simplessoproxy.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import dev.heinzl.simplessoproxy.models.Credential;

@Component
public interface PersistentCredentialsRepository extends CredentialsRepository {
    @Query(value = "{ 'app._id' : ObjectId(?0) }")
    public List<Credential> findByAppId(String id);
}
