package com.demo.test2;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(uri = "app://app2/service2")
public class TestService2 implements CsService {
    @Override
    public UriRespond call(UriRequest uriRequest) {
        return new UriRespond("这是我第二个服务");
    }
}
