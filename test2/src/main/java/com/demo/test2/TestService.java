package com.demo.test2;

import com.cugkuan.cs.core.CsService;
import com.cugkuan.cs.core.UriRequest;
import com.cugkuan.cs.core.UriRespond;
import com.cugkuan.cs.core.annotation.CsUri;

@CsUri(uri = "app://app2/service1")
public class TestService implements CsService {
    @Override
    public UriRespond call(UriRequest uriRequest) {
        return new UriRespond("我去其它的组件拿取东西");
    }
}
