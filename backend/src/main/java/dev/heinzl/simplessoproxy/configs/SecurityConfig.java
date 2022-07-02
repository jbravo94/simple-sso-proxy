package dev.heinzl.simplessoproxy.configs;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.repositories.UsersRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
            JwtTokenProvider tokenProvider,
            ReactiveAuthenticationManager reactiveAuthenticationManager) {
        final String PATH_POSTS = "/api/**";

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        .pathMatchers("/api/v1/auth/login").permitAll()
                        .pathMatchers(PATH_POSTS).authenticated()
                        .pathMatchers("/me").authenticated()
                        .pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
                        .anyExchange().permitAll())
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
            AuthorizationContext context) {

        return authentication
                .map(a -> context.getVariables().get("user").equals(a.getName()))
                .map(AuthorizationDecision::new);

    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(UsersRepository users) {

        return username -> Mono.just(users.findByUsername(username).stream()
                .map(u -> User
                        .withUsername(u.getUsername()).password(u.getPassword())
                        .authorities(u.getRoles().toArray(new String[0]))
                        .accountExpired(!u.isActive())
                        .credentialsExpired(!u.isActive())
                        .disabled(!u.isActive())
                        .accountLocked(!u.isActive())
                        .build())
                .findFirst().get());
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    /*
     * @Bean
     * public SecurityWebFilterChain securityWebFilterChain(
     * ServerHttpSecurity http) {
     * return http
     * .authorizeExchange()
     * .pathMatchers("/api/v1/login")
     * .permitAll()
     * .pathMatchers("/**")
     * .authenticated()
     * .and()
     * .httpBasic()
     * .and()
     * .csrf().disable()
     * .addFilterAfter(authenticationWebFilter(),
     * SecurityWebFiltersOrder.REACTOR_CONTEXT)
     * .build();
     * }
     * 
     * /*
     * .csrf(csrf ->
     * csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
     * )
     *
     * 
     * public AuthenticationWebFilter authenticationWebFilter() {
     * return new AuthenticationWebFilter(resolver());
     * }
     * 
     * public ReactiveAuthenticationManagerResolver<ServerWebExchange> resolver() {
     * return exchange -> {
     * return Mono.just(customersAuthenticationManager());
     * };
     * }
     * 
     * public ReactiveAuthenticationManager customersAuthenticationManager() {
     * return authentication -> customer(authentication)
     * .switchIfEmpty(Mono.error(new UsernameNotFoundException(authentication
     * .getPrincipal()
     * .toString())))
     * .map(b -> new
     * UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
     * authentication.getCredentials(),
     * Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
     * }
     * 
     * public Mono<String> customer(Authentication authentication) {
     * return Mono.justOrEmpty(authentication
     * .getPrincipal()
     * .toString()
     * .startsWith("user")
     * ? authentication
     * .getPrincipal()
     * .toString()
     * : null);
     * }
     * 
     * @Bean
     * public MapReactiveUserDetailsService userDetailsService() {
     * UserDetails user = User
     * .withUsername("user")
     * .password(passwordEncoder().encode("password"))
     * .roles("USER")
     * .build();
     * 
     * UserDetails admin = User
     * .withUsername("admin")
     * .password(passwordEncoder().encode("password"))
     * .roles("ADMIN")
     * .build();
     * 
     * return new MapReactiveUserDetailsService(user, admin);
     * }
     * 
     * @Bean
     * public PasswordEncoder passwordEncoder() {
     * return new BCryptPasswordEncoder();
     * }
     */
}