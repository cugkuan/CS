package com.demo.test2;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(uri = "app://app2/service1")
public class TestService implements CsService {
    @Override
    public UriRespond call(UriRequest uriRequest) {
        return new UriRespond("我去其它的组件拿取东西");
    }
}
