package com.brightk.cs;

import android.net.Uri;

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

}
