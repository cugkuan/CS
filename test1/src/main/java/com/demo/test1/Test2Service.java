package com.demo.test1;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.ServiceType;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(uri = "app://app1/service2")
public class Test2Service implements CsService {
    @Override
    public void call(UriRequest uriRequest, OnRequestResultListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listener.result(new UriRespond("的点点滴滴归根到底咯咯哒更大更大大"));
            }
        }).start();

    }
}
