package dev.heinzl.simplessoproxy.apps;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppsRepository extends MongoRepository<App, String> {
    List<App> findByName(String name);
}