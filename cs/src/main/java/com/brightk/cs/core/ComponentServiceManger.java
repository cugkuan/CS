package com.brightk.cs.core;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ComponentServiceManger {

    private static ConcurrentMap<String, String> csConfig = new ConcurrentHashMap<>();


    public static void pluginRegister(String key, @NonNull String c) {
        csConfig.put(key, c);
    }

    /**
     * 手动注册服务
     *
     * @param service
     */
    public static void register(String uri, Class<CsService> service, ServiceType serviceType) {
        String key = CsUtils.getKey(uri);
        String config = service.getName() + serviceType.getFlag();
        csConfig.put(key, config);
    }

    public static void register(String uri, Class<CsService> service) {
        register(uri, service, ServiceType.DEFAULT);
    }

    public static void unRegister(String uri) {
        String key = CsUtils.getKey(uri);
        csConfig.remove(key);
    }

    private static ServiceType getServiceType(char flag) {
        if (flag == ServiceType.DEFAULT.getFlag()) {
            return ServiceType.DEFAULT;
        } else if (flag == ServiceType.NEW.getFlag()) {
            return ServiceType.NEW;
        } else if (flag == ServiceType.SINGLE.getFlag()) {
            return ServiceType.SINGLE;
        } else {
            return ServiceType.DEFAULT;
        }
    }

    /**
     * @param key
     * @return
     * @hide
     */
    public static @Nullable
    ServiceConfig get(String key) {
        String config = csConfig.get(key);
        if (!TextUtils.isEmpty(config)) {
            try {
                int index = config.length() - 1;
                char flag = config.charAt(index);
                String className = config.substring(0, index);
                Class<CsService> c = (Class<CsService>) Class.forName(className);
                if (c != null) {
                    ServiceType type = getServiceType(flag);
                    return new ServiceConfig(c, type);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
