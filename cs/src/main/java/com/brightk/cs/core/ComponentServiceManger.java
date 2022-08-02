package com.brightk.cs.core;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ComponentServiceManger {

    private static ConcurrentMap<String,String> csConfig = new ConcurrentHashMap<>();

    private static ConcurrentMap<String,CsService> csServices = new ConcurrentHashMap<>();

    public static void register(String key,String c){
        csConfig.put(key,c);
    }


    public static void register(String key,CsService service){
        csServices.put(key,service);
    }

    public  static @Nullable CsService getService(String key){
        CsService service = csServices.get(key);
        if (service == null){
           String className = csConfig.get(key);
            if (!TextUtils.isEmpty(className)) {
                try {
                    service = (CsService) Class.forName(className).newInstance();
                    csConfig.remove(key);
                    register(key,service);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
        return  service;
    }
}
