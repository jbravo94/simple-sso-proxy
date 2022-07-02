package dev.heinzl.simplessoproxy.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.heinzl.simplessoproxy.models.User;
import dev.heinzl.simplessoproxy.repositories.UsersRepository;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UsersRepository users;

    private final PasswordEncoder passwordEncoder;

    // @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        Flux.just("user", "admin").flatMap(username -> {
            List<String> roles = "user".equals(username) ? Arrays.asList("ROLE_USER")
                    : Arrays.asList("ROLE_USER", "ROLE_ADMIN");
            User user = User.builder()
                    .roles(roles)
                    .username(username)
                    .password(passwordEncoder.encode("password"))
                    .email(username + "@example.com")
                    .build();
            this.users.save(user);
            return Flux.empty();
        });
    }
}