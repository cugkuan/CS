package com.demo.test1;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(uri = "app://app1/service1")
public class Test1Service implements CsService {
    @Override
    public UriRespond call(UriRequest uriRequest) {
        return new UriRespond("我是Test1中的服务");
    }
}
