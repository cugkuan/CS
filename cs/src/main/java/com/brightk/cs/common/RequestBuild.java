package com.brightk.cs.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.brightk.cs.CS;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数构建
 */
public abstract class RequestBuild {
    private Uri.Builder uriBuilder;
    private Uri uri;
    private WeakReference<Context> context;
    private Map<String, Object> params;
    public RequestBuild(String url) {
        this.uri = Uri.parse(url);
    }

    public RequestBuild(Uri uri) {
        this.uri = uri;
    }

    public RequestBuild addQueryParams(String key, String value) {
        if (uriBuilder == null) {
            uriBuilder = uri.buildUpon();
        }
        uriBuilder.appendQueryParameter(key, value);
        return this;
    }

    public RequestBuild setContext(Context context) {
        this.context = new WeakReference(context);
        return this;
    }

    public RequestBuild setAction(String action) {
        addParam(UriRequest.REQUEST_PARAMS_KEY_ACTION, action);
        return this;
    }

    public RequestBuild setBundle(Bundle bundle) {
        addParam(UriRequest.REQUEST_PARAMS_KEY_BUNDLE, bundle);
        return this;
    }

    public RequestBuild addParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }
    public RequestBuild putParams(Map<String, Object> params) {
        if (this.params == null) {
            this.params = params;
        } else {
            this.params.putAll(params);
        }
        return this;
    }
    protected UriRequest build() {
        if (uriBuilder != null){
            uri = uriBuilder.build();
        }
        Context c = null;
        if (context != null){
            c  = context.get();
        }
        UriRequest request = new UriRequest(c, uri);
        request.putParams(params);
        return request;
    }

    public void cancel() throws NotSupportCancelException {
        throw new NotSupportCancelException();
    }
    public void call(OnRequestResultListener listener) {
        CS.call(build(), listener);
    }
    public void call() {
        call(null);
    }

}
