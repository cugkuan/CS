package com.brightk.cs;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.brightk.cs.common.CsServiceManger;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

/**
 * CS - component service 的缩写
 * 轻量级的组件通信框架
 */
public class CS {

    public static int CS_CODE_SUCCEED = 200;
    public static int CS_CODE_NOT_FIND = 404;
    /**
     * 服务器内部错误
     */
    public static int CS_CODE_SERVICE_ERROR = 500;

    public static int CS_CODE_SERVICE_TIMEOUT = 501;
    /**
     * context 缺失了
     */
    public static int CS_CODE_SERVICE_CONTEXT_OUT = 501;
    /**
     * 缺少参数
     */
    public static int CS_CODE_SERVICE_LACK_PARAMS = 502;
    public static int CS_CODE_SERVICE_CANCEL_FAILURE = 600;


    /**
     *  服务自动注册的入口，通过ASM将代码注入到这里
     */
    public static void init(){

    }


    public static void call(UriRequest request, @Nullable OnRequestResultListener listener) {
        CsService service = getService(request.getUri());
        if (service != null) {
            service.call(request, listener);
        } else {
            if (listener != null) {
                listener.result(UriRespond.NOTFIND(request));
            }
        }
    }

    public static @Nullable
    CsService getService(Uri uri) {
        return CsServiceManger.getInstance().getService(uri);
    }

    public static @Nullable
    CsService getService(String uri) {
        return getService(Uri.parse(uri));
    }
}
