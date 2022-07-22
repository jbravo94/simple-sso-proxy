package dev.heinzl.simplessoproxy.users;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);
}