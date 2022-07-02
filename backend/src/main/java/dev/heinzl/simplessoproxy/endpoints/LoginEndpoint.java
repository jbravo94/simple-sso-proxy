package dev.heinzl.simplessoproxy.endpoints;

import java.security.Principal;
import java.time.Duration;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;

import dev.heinzl.simplessoproxy.models.User;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "/api/v1")
public class LoginEndpoint {

    // @Autowired
    // @Qualifier("authenticationManager")
    // protected AuthenticationManager authenticationManager;

    @Autowired
    private ReactiveAuthenticationManager reactiveAuthenticationManager;

    public LoginEndpoint() {
        super();
    }

    /*
     * @RequestMapping(value = "/login", method = RequestMethod.POST)
     * public void login(@RequestParam("username") final String username,
     * 
     * @RequestParam("password") final String password) {
     * /*
     * UsernamePasswordAuthenticationToken authReq = new
     * UsernamePasswordAuthenticationToken(username, password);
     * Authentication auth = authenticationManager.authenticate(authReq);
     * SecurityContext sc = SecurityContextHolder.getContext();
     * sc.setAuthentication(auth);
     *
     * }
     */

    @PostMapping("/login")
    public void login(ServerWebExchange exchange) {

        addTokenHeader(exchange.getResponse(), null);

        // ReactiveSecurityContextHolder.getContext().block().setAuthentication(new
        // UsernamePasswordAuthenticationToken(principal, credentials));

        /*
         * return ReactiveSecurityContextHolder.getContext()
         * .map(SecurityContext::getAuthentication)
         * .map(Authentication::getPrincipal)
         * .cast(User.class)
         * .doOnNext(userDetails -> {
         * addTokenHeader(exchange.getResponse(), userDetails); // your job to code it
         * the way you want
         * });
         */
    }

    private void addTokenHeader(ServerHttpResponse response, UserDetails userDetails) {
        // Change domain, path, secure
        response.addCookie(ResponseCookie.from("simple-sso-proxy-token", "test").build());
    }

}