package com.brightk.cs.core;

import android.content.Context;
import android.net.Uri;

import com.brightk.cs.CS;

public class UriRequest {

    private Context context;
    public final Uri uri;

    public UriRequest(Context context,Uri uri){
        this.context = context;
        this.uri = uri;

    }

    public UriRespond connect(){
        String key =  CsUtils.getKey(uri);
        CsService csService = ComponentServiceManger.getService(key);
        if (csService != null){
            return csService.call(this);
        }else {
            return new UriRespond(CS.CS_CODE_NOT_FIND);
        }

    }
}
