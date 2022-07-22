package dev.heinzl.simplessoproxy.configs.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReactiveAuthenticationManagerResolver<C> {
    Mono<ReactiveAuthenticationManager> resolve(C context);
}