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
