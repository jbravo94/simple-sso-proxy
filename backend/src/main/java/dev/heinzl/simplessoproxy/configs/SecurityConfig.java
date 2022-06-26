package dev.heinzl.simplessoproxy.configs;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/**")
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .addFilterAfter(authenticationWebFilter(), SecurityWebFiltersOrder.REACTOR_CONTEXT)
                .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()))
                .build();
    }

    public AuthenticationWebFilter authenticationWebFilter() {
        return new AuthenticationWebFilter(resolver());
    }

    public ReactiveAuthenticationManagerResolver<ServerWebExchange> resolver() {
        return exchange -> {
            return Mono.just(customersAuthenticationManager());
        };
    }

    public ReactiveAuthenticationManager customersAuthenticationManager() {
        return authentication -> customer(authentication)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(authentication
                        .getPrincipal()
                        .toString())))
                .map(b -> new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                        authentication.getCredentials(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    public Mono<String> customer(Authentication authentication) {
        return Mono.justOrEmpty(authentication
                .getPrincipal()
                .toString()
                .startsWith("user")
                        ? authentication
                                .getPrincipal()
                                .toString()
                        : null);
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}