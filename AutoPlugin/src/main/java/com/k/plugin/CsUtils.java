package com.k.plugin;


import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 这是生成的key 的规则 是 Java的 URI;需要特别的注意
 * 这里的规则和 com.brightk.cs 中的逻辑保持一致，需要特别的注意
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


    public static String getMD5(String content) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("MD5");
            var1.update(content.getBytes());
            return getHashString(var1);
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder var1 = new StringBuilder();
        byte[] var2 = digest.digest();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            var1.append(Integer.toHexString(var5 >> 4 & 15));
            var1.append(Integer.toHexString(var5 & 15));
        }

        return var1.toString();
    }

}
