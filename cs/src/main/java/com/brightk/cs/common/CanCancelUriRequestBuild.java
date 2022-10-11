package com.brightk.cs.common;

import android.net.Uri;

import com.brightk.cs.CS;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

/**
 *  可以取消的请求构建,需要 {@link CsService} 实现 cancel
 */
public class CanCancelUriRequestBuild extends UriRequestBuild {
    private CsService service;
    private OnRequestResultListener listener;
    public CanCancelUriRequestBuild(String url) {
        super(url);
    }
    public CanCancelUriRequestBuild(Uri uri) {
        super(uri);
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
