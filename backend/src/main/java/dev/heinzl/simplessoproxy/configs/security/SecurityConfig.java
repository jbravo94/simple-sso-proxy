package dev.heinzl.simplessoproxy.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        ReactiveAuthenticationManager reactiveAuthenticationManager) {
                final String PATH_POSTS = "/api/**";

                return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .cors(CorsSpec::disable)
                                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                                .authenticationManager(reactiveAuthenticationManager)
                                .securityContextRepository(securityContextRepository())
                                .authorizeExchange(it -> it
                                                .pathMatchers(HttpMethod.OPTIONS, PATH_POSTS).permitAll()
                                                .pathMatchers("/api/v1/auth/login").permitAll()
                                                .pathMatchers("/**").authenticated()
                                                .pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
                                                .anyExchange().denyAll())
                                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider,
                                                securityContextRepository()),
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
        public ReactiveAuthenticationManager reactiveAuthenticationManager(
                        ReactiveUserDetailsService userDetailsService,
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