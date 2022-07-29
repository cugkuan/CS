package com.cugkuan.cs;

import android.content.Context;
import android.net.Uri;

import com.cugkuan.cs.core.ComponentServiceManger;
import com.cugkuan.cs.core.CsService;
import com.cugkuan.cs.core.UriRequest;
import com.cugkuan.cs.core.UriRespond;

public class CS {

    public static int CS_CODE_SUCCEED = 0;
    public static int CS_CODE_NOT_FIND = 404;

    public static void register(String key, Class<? extends CsService> c){
        ComponentServiceManger.register(key,c);
    }
    public static UriRespond startUri(Context context, Uri uri){
        return new UriRequest(context,uri).connect();
    }

    public static UriRespond startUri(Context context,String uri){
        return startUri(context,Uri.parse(uri));
    }

}
