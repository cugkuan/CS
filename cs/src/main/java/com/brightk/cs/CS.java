package com.brightk.cs;

import android.content.Context;
import android.net.Uri;

import com.brightk.cs.core.ComponentServiceManger;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

public class CS {

    public static int CS_CODE_SUCCEED = 0;
    public static int CS_CODE_NOT_FIND = 404;



    public static void register(String key, String className){
        ComponentServiceManger.register(key,className);
    }
    public static UriRespond startUri(Context context, Uri uri){
        return new UriRequest(context,uri).connect();
    }

    public static UriRespond startUri(Context context,String uri){
        return startUri(context,Uri.parse(uri));
    }

}
