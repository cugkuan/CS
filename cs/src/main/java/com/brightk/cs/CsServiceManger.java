package com.brightk.cs;

import android.net.Uri;
import android.util.LruCache;

import com.brightk.cs.core.ComponentServiceManger;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.CsUtils;


class CsServiceManger {
    public static int CACHE_MAX_SIZE = 50;
    private static volatile CsServiceManger singleton;
    public static CsServiceManger getInstance(){
        if (singleton == null) {
            synchronized (CsServiceManger.class) {
                if (singleton == null) {
                    singleton = new CsServiceManger();
                }
            }
        }
        return singleton;
    }

    private LruCache<String, CsService>  csServiceLruCache = new LruCache<String,CsService>(CACHE_MAX_SIZE){
        @Override
        protected CsService create(String key) {
            return ComponentServiceManger.createService(key);
        }
    };

    public CsService getService(Uri uri){
        String key = CsUtils.getKey(uri);
        return csServiceLruCache.get(key);
    }
}
