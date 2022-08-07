package dev.heinzl.simplessoproxy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookieUtils {
    public static String getValueFromSetCookieHeader(String header) {

        Pattern p = Pattern.compile(".*=(.*);.*");
        Matcher m = p.matcher(header);

        if (m.find() && m.group().length() > 0) {
            return m.group(1);
        }

        return null;
    }
}
