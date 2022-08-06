package dev.heinzl.simplessoproxy.testing;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class TestingAspects {

    @AfterReturning(value = "execution(* *..ScriptingApiImpl.executeRequest(..)) && args(request)", returning = "response")
    public void afterAdvice(JoinPoint joinPoint, HttpRequest request, HttpResponse<String> response) throws Throwable {

        log.debug("Request Method: " + request.method());
        log.debug("Request Headers: " + request.headers().toString());
        log.debug("Request Body: " + request.bodyPublisher().orElse(BodyPublishers.noBody()).toString());

        log.debug("Response Statuscode: " + response.statusCode());
        log.debug("Response Headers: " + response.headers().toString());
        log.debug("Response Body: " + response.body().toString());
    }
}
