package com.demo.test2;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(uri = "app://app2/service1")
public class TestService implements CsService {
    @Override
    public void call(UriRequest uriRequest, OnRequestResultListener listener) {
      listener.result(new UriRespond("我去其它的组件拿取东西:跨服务数据传输验：=>"));
    }
}
