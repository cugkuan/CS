package com.brightk.cs.core;

import androidx.annotation.NonNull;

/**
 * 拦截器
 */
public interface CsInterceptor {
    void process(@NonNull UriRequest request, @NonNull InterceptorCallback callback);
}
