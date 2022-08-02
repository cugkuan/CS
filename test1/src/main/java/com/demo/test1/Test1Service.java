package com.demo.test1;

import com.cugkuan.cs.core.CsService;
import com.cugkuan.cs.core.UriRequest;
import com.cugkuan.cs.core.UriRespond;
import com.cugkuan.cs.core.annotation.CsUri;

@CsUri(uri = "app://app1/service1")
public class Test1Service implements CsService {
    @Override
    public UriRespond call(UriRequest uriRequest) {
        return new UriRespond("我是Test1中的服务");
    }
}
