package com.brightk.cs.common;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;

import com.brightk.cs.CS;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 请求参数构建
 */
public class UriRequestBuild extends RequestBuild {
    //线程是否挂起
    private volatile boolean isWait = false;
    public UriRequestBuild(String url) {
        super(url);
    }
    public UriRequestBuild(Uri uri) {
        super(uri);
    }
    public UriRespond connect() {
        UriRequest request = build();
        AtomicReference<UriRespond> uriRespond = new AtomicReference<>();
        CS.call(request, respond -> {
            synchronized (request) {
                uriRespond.set(respond);
                if (isWait) {
                    try {
                        request.notify();
                    } catch (IllegalMonitorStateException e) {
                    }
                }
                isWait = false;
            }
        });
        synchronized (request) {
            while (uriRespond.get() == null) {
                if (Thread.currentThread() == Looper.getMainLooper().getThread()){
                    throw new RuntimeException("Cs:不能在主线中进行这样的操作");
                }
                try {
                    isWait = true;
                    request.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return uriRespond.get();
    }
}
