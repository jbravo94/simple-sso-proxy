package dev.heinzl.simplessoproxy.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.heinzl.simplessoproxy.models.App;

public interface AppsRepository extends MongoRepository<App, String> {
    List<App> findByName(String name);
}