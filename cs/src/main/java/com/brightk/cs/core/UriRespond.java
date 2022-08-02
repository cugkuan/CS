package com.brightk.cs.core;

import androidx.annotation.Nullable;

import com.brightk.cs.CS;

public class UriRespond {

    public final int code;
    @Nullable
    public final Object data;
    @Nullable
    public final Throwable throwable;

    public UriRespond(int code){
        this(code,null,null);
    }
    public UriRespond(Object data){
        this(CS.CS_CODE_SUCCEED,data,null);
    }
    public UriRespond(int code,@Nullable Object data){
        this(code,data,null);
    }
    public UriRespond(int code,Throwable throwable){
        this(code,null,throwable);
    }
    public UriRespond(int code, @Nullable Object data, @Nullable Throwable throwable) {
        this.code = code;
        this.data = data;
        this.throwable = throwable;
    }



}
