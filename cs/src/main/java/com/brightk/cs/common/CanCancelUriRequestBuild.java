package com.brightk.cs.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.brightk.cs.CS;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 *  可以取消的请求构建,需要 {@link CsService} 实现 cancel
 */
public class CanCancelUriRequestBuild  {
    private CsService service;
    private OnRequestResultListener listener;
    private Uri.Builder uriBuilder;
    private Uri uri;
    private WeakReference<Context> context;
    private Map<String, Object> params;

    public CanCancelUriRequestBuild(String url) {
        this.uri = Uri.parse(url);
    }
    public CanCancelUriRequestBuild(Uri uri) {
        this.uri = uri;
    }
    public CanCancelUriRequestBuild addQueryParams(String key, String value) {
        if (uriBuilder == null) {
            uriBuilder = uri.buildUpon();
        }
        uriBuilder.appendQueryParameter(key, value);
        return this;
    }

    public CanCancelUriRequestBuild setContext(Context context) {
        this.context = new WeakReference(context);
        return this;
    }

    public CanCancelUriRequestBuild setAction(String action) {
        addParam(UriRequest.REQUEST_PARAMS_KEY_ACTION, action);
        return this;
    }

    public CanCancelUriRequestBuild setBundle(Bundle bundle) {
        addParam(UriRequest.REQUEST_PARAMS_KEY_BUNDLE, bundle);
        return this;
    }

    public CanCancelUriRequestBuild addParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }
    public CanCancelUriRequestBuild putParams(Map<String, Object> params) {
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


    public void cancel(){
        if (service != null){
            service.cancel(listener);
        }else {
            if (listener != null){
                listener.result(new UriRespond(CS.CS_CODE_SERVICE_CANCEL_FAILURE,new Throwable("取消失败，服务没有找到或没有启动")));
            }
        }
    }
    public void call(OnRequestResultListener listener){
        UriRequest request = build();
        this.listener = listener;
        service  = CsServiceManger.getInstance().getNewService(request.getUri());
        if (service != null){
            service.call(request,listener);
        }else {
            if (listener != null){
                listener.result(UriRespond.NOTFIND(request));
            }
        }
    }

    public void call(){
        call(null);
    }
}
