package com.cugkuan.cs.core;


import androidx.annotation.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ComponentServiceManger {

    private static ConcurrentMap<String,Class<? extends CsService>> csConfig = new ConcurrentHashMap<>();

    private static ConcurrentMap<String,CsService> csServices = new ConcurrentHashMap<>();

    public static void register(String key,Class<? extends  CsService> c){
        csConfig.put(key,c);
    }


    public static void register(String key,CsService service){
        csServices.put(key,service);
    }

    public  static @Nullable CsService getService(String key){
        CsService service = csServices.get(key);
        if (service == null){
            Class<? extends CsService>  csServiceClass = csConfig.get(key);
            if (csServiceClass != null) {
                try {
                    service = csServiceClass.newInstance();
                    csConfig.remove(key);
                    register(key,service);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return  service;
    }
}
