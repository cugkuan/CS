package com.brightk.cs;

import android.content.Context;
import android.net.Uri;

import com.brightk.cs.core.ComponentServiceManger;
import com.brightk.cs.core.CsService;
import com.brightk.cs.core.CsUtils;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.UriRespond;

/**
 * CS - component service 的缩写
 * 轻量级的组件通信框架
 */
public class CS {

    public static int CS_CODE_SUCCEED = 0;
    public static int CS_CODE_NOT_FIND = 404;

    /**
     * 服务器内部错误
     */
    public static int CS_CODE_SERVICE_ERROR = 500;
    /**
     * context 缺失了
     */
    public static int CS_CODE_SERVICE_CONTEXT_OUT = 501;
    /**
     * 缺少参数
     */
    public static int CS_CODE_SERVICE_LACK_PARAMS = 502;

    /**
     * 注册服务
     *
     * @param key
     * @param className
     */
    public static void register(String key, String className) {
        ComponentServiceManger.register(key, className);
    }

    protected static UriRespond connect(UriRequest request) {
        String key = CsUtils.getKey(request.getUri());
        CsService service = ComponentServiceManger.getService(key);
        if (service != null) {
            return service.call(request);
        } else {
            return new UriRespond(CS.CS_CODE_NOT_FIND);
        }
    }

    public static UriRespond startRequest(UriRequest request) {
        return connect(request);
    }

    public static UriRespond startUri(Context context, Uri uri) {
        return startRequest(new UriRequest(context, uri));
    }

    public static UriRespond startUri(Context context, String uri) {
        return startUri(context, Uri.parse(uri));
    }

    public static UriRespond startUri(String uri) {
        return startUri(null, uri);
    }

    public static UriRespond startUri(Uri uri) {
        return startUri(null, uri);
    }


}
