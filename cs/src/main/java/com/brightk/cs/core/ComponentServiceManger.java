package com.brightk.cs.core;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brightk.cs.core.annotation.CsUri;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ComponentServiceManger {

    private static ConcurrentMap<String, String> csConfig = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, Class<CsService>> csServices = new ConcurrentHashMap<>();

    public static void register(String key, @NonNull String c) {
        csConfig.put(key, c);
    }

    /**
     * 手动注册服务
     * @param service
     */
    public static void register(Class<CsService> service) {
        CsUri csUri = service.getAnnotation(CsUri.class);
        if (csUri == null){
            throw new NullPointerException("CsUri没有配置");
        }else {
            String uri = csUri.uri();
            String key = CsUtils.getKey(uri);
            csServices.put(key, service);
        }
    }


    /**
     * @param key
     * @return
     * @hide
     */
    public static @Nullable
     synchronized CsService createService(String key) {
        Class<CsService> c = csServices.get(key);
        if (c == null) {
            String className = csConfig.get(key);
            if (!TextUtils.isEmpty(className)) {
                try {
                    c = (Class<CsService>) Class.forName(className);
                    csServices.put(key, c);
                    csConfig.remove(key);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (c != null) {
            try {
                return c.getDeclaredConstructor().newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
