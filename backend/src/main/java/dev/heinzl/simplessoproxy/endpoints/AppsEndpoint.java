package dev.heinzl.simplessoproxy.endpoints;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.heinzl.simplessoproxy.models.App;
import dev.heinzl.simplessoproxy.repositories.AppsRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/apps")
public class AppsEndpoint {

    @Autowired
    AppsRepository appsRepository;

    @GetMapping("/all")
    public Flux<App> getAll() {
        return Flux.fromIterable(appsRepository.findAll());
    }

    @PostMapping
    public Mono<App> create(App app) {
        return Mono.just(appsRepository.save(app));
    }

    @PutMapping("/{id}")
    public Mono<App> update(App app, @PathVariable("id") String id) {

        Optional<App> originalAppOptional = appsRepository.findById(id);

        if (originalAppOptional.isPresent()) {
            App originalApp = originalAppOptional.get();

            if (app.getName() != null) {
                originalApp.setName(app.getName());
            }

            if (app.getBaseUrl() != null) {
                originalApp.setBaseUrl(app.getBaseUrl());
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

            return Mono.just(appsRepository.save(app));
        } else {
            return Mono.error(new NotFoundException("No App found with fiven id."));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        appsRepository.deleteById(id);
    }
}
