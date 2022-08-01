package com.demo.test2;

import com.cugkuan.cs.core.CsService;
import com.cugkuan.cs.core.UriRequest;
import com.cugkuan.cs.core.UriRespond;
import com.cugkuan.cs.core.annotation.CsUri;

@CsUri(uri = "app://app2/service2")
public class TestService2 implements CsService {
    @Override
    public UriRespond call(UriRequest uriRequest) {
        return new UriRespond("这是我第二个服务");
    }
}
