package com.k.plugin;


import java.net.URI;

/**
 * 这是生成的key 的规则 是 Java的 URI;需要特别的注意
 * 这里的规则和 cs 中的逻辑保持一致，需要特别的注意
 */
public class CsUtils {


    private static String checkNull(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static String getKey(URI uri) {
        return new StringBuilder()
                .append(checkNull(uri.getScheme()))
                .append("-")
                .append(checkNull(uri.getAuthority()))
                .append("-")
                .append(checkNull(uri.getPath())).toString();
    }

    public static String getKey(String url) {
        return getKey(URI.create(url));
    }
}
