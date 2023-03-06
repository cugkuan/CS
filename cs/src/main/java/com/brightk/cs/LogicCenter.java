package com.brightk.cs;

import android.net.Uri;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brightk.cs.common.CsServiceManger;
import com.brightk.cs.core.CsException;
import com.brightk.cs.core.CsInterceptor;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.InterceptorCallback;
import com.brightk.cs.core.InterceptorManger;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LogicCenter {
    public static @Nullable
    CsService getService(Uri uri) {
        return CsServiceManger.getInstance().getService(uri);
    }
    public static @Nullable
    CsService getService(String uri) {
        return getService(Uri.parse(uri));
    }

    private static void interceptor(int index, UriRequest request, @Nullable OnRequestResultListener listener, List<CsInterceptor> interceptors) {
        if (index < interceptors.size()) {
            CsInterceptor csInterceptor = interceptors.get(index);
            csInterceptor.process(request, new InterceptorCallback() {
                @Override
                public void onNext(@NonNull UriRequest request) {
                    interceptor(index + 1, request, listener, interceptors);
                }
                @Override
                public void onError(@NonNull CsException e) {
                    if (listener != null) {
                        listener.result(new UriRespond(CS.CS_CODE_INTERCEPTOR_FAILURE,e));
                    }
                }
            });
        } else {
            callService(request, listener);
        }
    }

    public static void call(UriRequest request, @Nullable OnRequestResultListener listener) {
        List<CsInterceptor> interceptors = InterceptorManger.getInstance().getInterceptors();
        if (interceptors.isEmpty()) {
            callService(request, listener);
        } else {
            interceptor(0, request, listener, interceptors);
        }
    }

    private static void callService(UriRequest request, @Nullable OnRequestResultListener listener) {
        CsService service = getService(request.getUri());
        if (service != null) {
            service.call(request, listener);
        } else {
            if (listener != null) {
                listener.result(UriRespond.NOTFIND(request));
            }
        }
    }

    public static UriRespond connect(UriRequest request){
        AtomicReference<UriRespond> uriRespond = new AtomicReference<>();
        CS.call(request, respond -> {
            synchronized (request) {
                if (respond == null){
                    uriRespond.set(new UriRespond(CS.CS_CODE_RESPOND_NULL,new NullPointerException("Cs:组件服务中没有返回 respond")));
                }else {
                    uriRespond.set(respond);
                }
                request.notifyAll();
            }
        });
        synchronized (request) {
            while (uriRespond.get() == null) {
                if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                    throw new RuntimeException("Cs:不能在主线中进行这样的操作，或者检测CsInterceptor,没有callback.onNext();callback.onError()");
                }
                try {
                    request.wait();
                } catch (InterruptedException e) {
                    uriRespond.set(new UriRespond(CS.CS_CODE_RESPOND_NULL,e));
                    e.printStackTrace();

                }
            }
        }
        return uriRespond.get();
    }

}
