package dev.heinzl.simplessoproxy.credentials;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PersistentCredentialsRepositoryImpl
        extends CredentialsRepository, MongoRepository<Credential, String> {
    @Query(value = "{ 'app._id' : ObjectId(?0) }")
    public List<Credential> findByAppId(String id);

    @Query(value = "{ 'app._id' : ObjectId(?0), 'user._id' : ObjectId(?1) }")
    public List<Credential> findByAppIdAndUserId(String appId, String userId);
}