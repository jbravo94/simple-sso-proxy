package dev.heinzl.simplessoproxy.repositories;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import dev.heinzl.simplessoproxy.models.Credential;
import lombok.AllArgsConstructor;

@NoRepositoryBean
@AllArgsConstructor
public class InMemoryCredentialsRepositoryDecorator implements CredentialsRepository {

    private final CredentialsRepository credentialsRepository;
    private final SecretsRepository secretsRepository;

    @Override
    public <S extends Credential> S save(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Credential> Iterable<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Credential> findById(String id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public boolean existsById(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterable<Credential> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Credential> findAllById(Iterable<String> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deleteById(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Credential entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll(Iterable<? extends Credential> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Credential> findByAppId(String id) {
        // TODO Auto-generated method stub
        return null;
    }

}