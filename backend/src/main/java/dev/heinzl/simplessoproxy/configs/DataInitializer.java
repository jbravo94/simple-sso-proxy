package dev.heinzl.simplessoproxy.configs;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.heinzl.simplessoproxy.users.User;
import dev.heinzl.simplessoproxy.users.UsersRepository;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UsersRepository users;

    private final PasswordEncoder passwordEncoder;

    // @EventListener(value = ApplicationReadyEvent.class)
    public void init() {

        User user = User.builder()
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .username("superman")
                .password(passwordEncoder.encode("Admin123"))
                .email("superman@example.com")
                .build();
        this.users.save(user);
    }
}