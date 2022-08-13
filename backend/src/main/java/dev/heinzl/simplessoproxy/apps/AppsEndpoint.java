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
package dev.heinzl.simplessoproxy.apps;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.heinzl.simplessoproxy.configs.routing.GatewayRouteService;
import dev.heinzl.simplessoproxy.credentials.CredentialsRepository;
import dev.heinzl.simplessoproxy.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/apps")
public class AppsEndpoint {

    @Autowired
    AppsRepository appsRepository;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    UsersRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GatewayRouteService gatewayRouteService;

    private final Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();

    private Mono<SecurityContext> getAuthenticatedContexts(Mono<SecurityContext> context) {
        return context.filter(c -> Objects.nonNull(c.getAuthentication()));
    }

    @CrossOrigin
    @GetMapping("/all")
    public Flux<App> getAll() {
        return getAuthenticatedContexts(context).flux()
                .flatMap(f -> {
                    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) f
                            .getAuthentication().getPrincipal();
                    log.info(user.getUsername());
                    return Flux.fromIterable(appsRepository.findAll());
                });
    }

    @PostMapping
    public Mono<App> create(@RequestBody App app) {
        App createApp = appsRepository.save(app);

        gatewayRouteService.refreshRoutes();

        return Mono.just(createApp);
    }

    @PutMapping("/{id}")
    public Mono<App> update(@RequestBody App app, @PathVariable("id") String id) {

        Optional<App> originalAppOptional = appsRepository.findById(id);

        if (originalAppOptional.isPresent()) {
            App originalApp = originalAppOptional.get();

            if (app.getName() != null) {
                originalApp.setName(app.getName());
            }

            if (app.getBaseUrl() != null) {
                originalApp.setBaseUrl(app.getBaseUrl());
            }

            if (app.getProxyUrl() != null) {
                originalApp.setProxyUrl(app.getProxyUrl());
            }

            if (app.getLoginScript() != null) {
                originalApp.setLoginScript(app.getLoginScript());
            }

            if (app.getLogoutScript() != null) {
                originalApp.setLogoutScript(app.getLogoutScript());
            }

            if (app.getProxyScript() != null) {
                originalApp.setProxyScript(app.getProxyScript());
            }

            if (app.getResetScript() != null) {
                originalApp.setResetScript(app.getResetScript());
            }

            App updatedApp = appsRepository.save(app);

            gatewayRouteService.refreshRoutes();

            return Mono.just(updatedApp);
        } else {
            return Mono.error(new NotFoundException("No App found with fiven id."));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        appsRepository.deleteById(id);
        gatewayRouteService.refreshRoutes();
    }
}
