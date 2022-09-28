package com.demo.test2;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.ServiceType;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(type = ServiceType.SINGLE, uri = "app://app2/service2")
public class TestService2 implements CsService {
    @Override
    public void call(UriRequest uriRequest, OnRequestResultListener listener) {
        UriRespond respond = new  UriRespond("这是我第二个服务");
        listener.result(respond);
    }
}
