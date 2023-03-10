package com.demo.test1;

import android.util.Log;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.ServiceType;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(type = ServiceType.SINGLE,uri = "app://app1/service1")
public class Test1Service implements CsService {
    @Override
    public void call(UriRequest uriRequest, OnRequestResultListener listener) {

        Log.e("lmk","--------------");
        listener.result(new UriRespond("我是Test1中的服务"));
    }
}
