package com.brightk.cs.core;

import android.net.Uri;

/**
 * key 生成的规则必须和AutoPlugin 生成的规则一致性
 * 改动这里的代码注意同步改
 */
public class CsUtils {


    private static String checkNull(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static String getKey(Uri uri) {
        return new StringBuilder()
                .append(checkNull(uri.getScheme()))
                .append("-")
                .append(checkNull(uri.getAuthority()))
                .append("-")
                .append(checkNull(uri.getPath())).toString();
    }

    public static String getKey(String url) {
        return getKey(Uri.parse(url));
    }
}
