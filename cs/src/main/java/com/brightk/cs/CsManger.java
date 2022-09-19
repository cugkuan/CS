package com.brightk.cs;

import android.net.Uri;
import android.util.LruCache;

import com.brightk.cs.core.ComponentServiceManger;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.CsUtils;


class CsManger {
    public static int CACHE_MAX_SIZE = 5;
    private static volatile CsManger singleton;
    public static CsManger getInstance(){
        if (singleton == null) {
            synchronized (CsManger.class) {
                if (singleton == null) {
                    singleton = new CsManger();
                }
            }
        }
        return singleton;
    }

    private LruCache<String, CsService>  csServiceLruCache = new LruCache<String,CsService>(CACHE_MAX_SIZE){

        @Override
        protected CsService create(String key) {
            return ComponentServiceManger.getService(key);
        }
    };

    public CsService getService(Uri uri){
        String key = CsUtils.getKey(uri);
        return csServiceLruCache.get(key);
    }
}
