package dev.heinzl.simplessoproxy.repositories;

import java.util.List;
import java.util.Optional;

import dev.heinzl.simplessoproxy.models.Credential;

public class PersistentCredentialsRepository implements CredentialsRepository {

    @Override
    public List<Credential> findByAppId(String id) {
        // TODO Auto-generated method stub
        return null;
    }

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
}