package com.brightk.cs.core;

import android.net.Uri;

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
