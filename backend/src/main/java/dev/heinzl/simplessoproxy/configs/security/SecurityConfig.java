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
package dev.heinzl.simplessoproxy.configs.security;

import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenAuthenticationFilter;
import dev.heinzl.simplessoproxy.configs.jwt.JwtTokenProvider;
import dev.heinzl.simplessoproxy.users.UsersRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

        ObjectMapper mapper = new ObjectMapper();

        @Bean
        SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                        JwtTokenProvider tokenProvider,
                        ReactiveAuthenticationManager reactiveAuthenticationManager,
                        Environment env) {
                final String PATH_POSTS = "/api/**";

                boolean isDevProfile = Stream.of(env.getActiveProfiles()).anyMatch(profile -> profile.equals("dev"));

                final String[] swaggerPaths = new String[] { "/v2/api-docs",
                                "/configuration/ui",
                                "/swagger-resources/**",
                                "/configuration/security",
                                "/swagger-ui.html",
                                "/webjars/**" };

                return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .cors(CorsSpec::disable)
                                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                                .authenticationManager(reactiveAuthenticationManager)
                                .securityContextRepository(securityContextRepository())
                                .authorizeExchange(it -> {
                                        if (isDevProfile) {
                                                it.pathMatchers(swaggerPaths).permitAll();
                                        }

                                        it.pathMatchers(HttpMethod.OPTIONS, PATH_POSTS).permitAll()
                                                        .pathMatchers("/api/v1/auth/login").permitAll()
                                                        .pathMatchers("/**").authenticated()
                                                        .pathMatchers("/users/{user}/**")
                                                        .access(this::currentUserMatchesPath)
                                                        .anyExchange().denyAll();
                                })
                                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider),
                                                SecurityWebFiltersOrder.HTTP_BASIC)

                                .build();
        }

        @Bean
        public ServerSecurityContextRepository securityContextRepository() {
                WebSessionServerSecurityContextRepository securityContextRepository = new WebSessionServerSecurityContextRepository();

                securityContextRepository.setSpringSecurityContextAttrName("simple-sso-proxy-security-context");

                return securityContextRepository;
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

                return username -> Mono.just(Stream.of(users.findByUsername(username))
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
        public ReactiveAuthenticationManager reactiveAuthenticationManager(
                        ReactiveUserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
                authenticationManager.setPasswordEncoder(passwordEncoder);
                return authenticationManager;
        }

}