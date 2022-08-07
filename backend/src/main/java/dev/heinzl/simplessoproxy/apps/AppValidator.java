package dev.heinzl.simplessoproxy.apps;

import java.net.URI;
import java.net.URISyntaxException;

import dev.heinzl.simplessoproxy.utils.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppValidator implements Validator<App> {

    @Override
    public boolean isValid(App entity) {

        if (entity == null) {
            return false;
        }

        try {
            new URI(entity.getBaseUrl());
        } catch (URISyntaxException e) {
            logUrlMessage(e.getMessage());

            return false;
        }

        try {
            new URI(entity.getProxyUrl());
        } catch (URISyntaxException e) {
            logUrlMessage(e.getMessage());

            return false;
        }

        return false;
    }

    private void logUrlMessage(String message) {
        log.info("URL malformatted: " + message);
    }

    private AppValidator() {
    }

    private static AppValidator appValidator;

    public static AppValidator getInstance() {
        if (appValidator == null) {
            appValidator = new AppValidator();
        }
        return appValidator;
    }

}
