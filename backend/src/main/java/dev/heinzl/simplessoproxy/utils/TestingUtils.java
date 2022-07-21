package dev.heinzl.simplessoproxy.utils;

import java.util.List;

import org.springframework.web.server.ServerWebExchange;

public class TestingUtils {

    public static void modifyBahmniCookie(ServerWebExchange exchange) {
        List<String> list = exchange.getResponse().getHeaders().get("Set-Cookie");

        if (list != null && list.size() > 0) {

            String value = list.get(0);

            value = value.replace(" domain=demo.mybahmni.org;", "");
            value = value.replace(" secure;", "");

            exchange.getResponse().getHeaders().set("Set-Cookie", value);
        }
    }
}
