/*
 * The MIT License
 * Copyright © 2022 Johannes HEINZL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.heinzl.simplessoproxy.credentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.repository.NoRepositoryBean;

import com.google.common.collect.Lists;

import dev.heinzl.simplessoproxy.secrets.InMemorySecretsRepositoryImpl;
import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;

@NoRepositoryBean
@AllArgsConstructor
public class InMemoryCredentialsRepositoryDecorator implements CredentialsRepository {

    private final CredentialsRepository credentialsRepository;
    private final InMemorySecretsRepositoryImpl inMemorySecretsRepository;

    private Iterable<Credential> decorateCredentials(Iterable<Credential> credentials) {
        return Stream.ofAll(credentials).map(this::decorateCredential).collect(Collectors.toList());
    }

    private Credential decorateCredential(Credential credential) {

        if (credential == null || credential.getId() == null) {
            throw new IllegalStateException("Credential must not be null and provide an id.");
        }

        credential.setSecret(this.inMemorySecretsRepository.getSecret(credential.getId()));
        return credential;
    }

    @Override
    public <S extends Credential> S save(S entity) {

        if (entity == null) {
            throw new IllegalStateException("Entity must not be null.");
        }

        S credential = (S) entity.withSecret(null);

        S save = this.credentialsRepository.save(credential);

        this.inMemorySecretsRepository.setSecret(save.getId(), entity.getSecret());

        return save;
    }

    @Override
    public <S extends Credential> Iterable<S> saveAll(Iterable<S> entities) {

        if (entities == null) {
            throw new IllegalStateException("Entities must not be null.");
        }

        List<S> credentials = new ArrayList<>();

        for (S entity : entities) {
            credentials.add(this.save(entity));
        }

        return credentials;
    }

    @Override
    public Optional<Credential> findById(String id) {

        Optional<Credential> findById = this.credentialsRepository.findById(id);

        if (findById.isPresent()) {
            return Optional.of(this.decorateCredential(findById.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(String id) {
        return this.credentialsRepository.existsById(id);
    }

    @Override
    public Iterable<Credential> findAll() {
        Iterable<Credential> findAll = this.credentialsRepository.findAll();

        return this.decorateCredentials(findAll);
    }

    @Override
    public Iterable<Credential> findAllById(Iterable<String> ids) {

        Iterable<Credential> findAllById = this.credentialsRepository.findAllById(ids);

        return this.decorateCredentials(findAllById);
    }

    @Override
    public long count() {
        return this.credentialsRepository.count();
    }

    @Override
    public void deleteById(String id) {
        this.inMemorySecretsRepository.removeSecret(id);
        this.credentialsRepository.deleteById(id);
    }

    @Override
    public void delete(Credential entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalStateException("Entity must not be null and provide an id.");
        }

        String id = entity.getId();

        this.inMemorySecretsRepository.removeSecret(id);
        this.credentialsRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        if (ids == null) {
            throw new IllegalStateException("ids must not be null.");
        }

        ids.forEach(id -> {
            if (id == null) {
                throw new IllegalStateException("Id must not be null");
            }
        });

        ids.forEach(id -> {
            this.inMemorySecretsRepository.removeSecret(id);
            this.credentialsRepository.deleteById(id);
        });
    }

    @Override
    public void deleteAll(Iterable<? extends Credential> entities) {
        if (entities == null) {
            throw new IllegalStateException("Entities must not be null.");
        }

        entities.forEach(entity -> {
            if (entity == null || entity.getId() == null) {
                throw new IllegalStateException("Entity must not be null and provide an id.");
            }
        });

        entities.forEach(entity -> {
            String id = entity.getId();

            this.inMemorySecretsRepository.removeSecret(id);
            this.credentialsRepository.deleteById(id);
        });
    }

    @Override
    public void deleteAll() {
        this.inMemorySecretsRepository.removeAllSecrets();
        this.credentialsRepository.deleteAll();
    }

    @Override
    public List<Credential> findByAppId(String id) {

        List<Credential> findByAppId = this.credentialsRepository.findByAppId(id);

        return Lists.newArrayList(this.decorateCredentials(findByAppId));
    }

    @Override
    public List<Credential> findByAppIdAndUserId(String appId, String userId) {
        List<Credential> findByAppIdAndUserId = this.credentialsRepository.findByAppIdAndUserId(appId, userId);

        return Lists.newArrayList(this.decorateCredentials(findByAppIdAndUserId));
    }

}