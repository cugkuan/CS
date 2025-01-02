package com.demo.app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.brightk.cs.core.CsInterceptor;
import com.brightk.cs.core.InterceptorCallback;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.annotation.Interceptor;

@Interceptor(priority = Integer.MAX_VALUE, name = "log")
public class A implements CsInterceptor {

    @Override
    public void process(@NonNull UriRequest request, @NonNull InterceptorCallback callback) {

        Log.e("lmk", request.getUri().toString());
        callback.onNext(request);

    }
}
