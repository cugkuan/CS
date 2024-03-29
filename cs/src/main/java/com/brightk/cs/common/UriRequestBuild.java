package com.brightk.cs.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;

import com.brightk.cs.CS;
import com.brightk.cs.LogicCenter;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 请求参数构建
 */
public class UriRequestBuild {
    private Uri.Builder uriBuilder;
    private Uri uri;
    private WeakReference<Context> context;
    private Map<String, Object> params;

    public UriRequestBuild(String url) {
        this.uri = Uri.parse(url);
    }

    public UriRequestBuild(Uri uri) {
        this.uri = uri;
    }

    public UriRequestBuild addQueryParams(String key, String value) {
        if (uriBuilder == null) {
            uriBuilder = uri.buildUpon();
        }
        uriBuilder.appendQueryParameter(key, value);
        return this;
    }

    public UriRequestBuild setContext(Context context) {
        this.context = new WeakReference(context);
        return this;
    }

    public UriRequestBuild setAction(String action) {
        addParam(UriRequest.REQUEST_PARAMS_KEY_ACTION, action);
        return this;
    }

    public UriRequestBuild setBundle(Bundle bundle) {
        addParam(UriRequest.REQUEST_PARAMS_KEY_BUNDLE, bundle);
        return this;
    }

    public UriRequestBuild addParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public UriRequestBuild putParams(Map<String, Object> params) {
        if (this.params == null) {
            this.params = params;
        } else {
            this.params.putAll(params);
        }
        return this;
    }

    protected UriRequest build() {
        if (uriBuilder != null) {
            uri = uriBuilder.build();
        }
        Context c = null;
        if (context != null) {
            c = context.get();
        }
        UriRequest request = new UriRequest(c, uri);
        request.putParams(params);
        return request;
    }

    public void call(OnRequestResultListener listener) {
        LogicCenter.call(build(), listener);
    }

    public void call() {
        call(null);
    }

    public UriRespond connect() {
        UriRequest request = build();
        return LogicCenter.connect(request);

    }

}
