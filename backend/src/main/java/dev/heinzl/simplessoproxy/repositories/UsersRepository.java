package dev.heinzl.simplessoproxy.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.heinzl.simplessoproxy.models.User;
import reactor.core.publisher.Mono;

public interface UsersRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);
}