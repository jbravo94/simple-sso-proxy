package dev.heinzl.simplessoproxy.credentials;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CredentialsRepository extends CrudRepository<Credential, String> {
    List<Credential> findByAppId(String id);

    List<Credential> findByAppIdAndUserId(String appId, String userId);
}
