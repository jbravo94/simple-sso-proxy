package dev.heinzl.simplessoproxy.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

import org.springframework.web.server.ServerWebExchange;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    public static SSLContext insecureContext() {

        Properties properties = System.getProperties();
        properties.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

        TrustManager[] noopTrustManager = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] xcs, String string) {
                    }

                    public void checkServerTrusted(X509Certificate[] xcs, String string) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };

        SSLContext sc = null;

        try {
            sc = SSLContext.getInstance("ssl");
            sc.init(null, noopTrustManager, null);
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
        }

        return sc;
    }
}
