package com.brightk.cs;

import android.net.Uri;
import android.util.LruCache;

import com.brightk.cs.core.ComponentServiceManger;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.CsUtils;
import com.brightk.cs.core.ServiceConfig;
import com.brightk.cs.core.ServiceType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


class CsServiceManger {
    public static int CACHE_MAX_SIZE = 50;
    private static volatile CsServiceManger singleton;

    public static CsServiceManger getInstance() {
        if (singleton == null) {
            synchronized (CsServiceManger.class) {
                if (singleton == null) {
                    singleton = new CsServiceManger();
                }
            }
        }
        return singleton;
    }

    private Map<String, CsService> single = new ConcurrentHashMap<>();
    private LruCache<String, ServiceConfig> csServiceLruCache = new LruCache<String, ServiceConfig>(CACHE_MAX_SIZE) {
        @Override
        protected ServiceConfig create(String key) {
            ServiceConfig config = ComponentServiceManger.get(key);
            if (config.type == ServiceType.DEFAULT) {
                config.setService(createService(config.serviceClass));
            } else if (config.type == ServiceType.SINGLE) {
                CsService csService = single.get(key);
                if (csService == null) {
                    CsService service = createService(config.serviceClass);
                    single.put(key, service);
                    config.setService(service);
                }
            }
            return config;
        }
    };

    private synchronized CsService createService(Class<CsService> c) {
        try {
            CsService service = c.getDeclaredConstructor().newInstance();
            return service;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CsService getService(Uri uri) {
        String key = CsUtils.getKey(uri);
        ServiceConfig config = csServiceLruCache.get(key);
        if (config.type == ServiceType.NEW) {
            return createService(config.serviceClass);
        } else {
            return config.getService();
        }
    }
}
