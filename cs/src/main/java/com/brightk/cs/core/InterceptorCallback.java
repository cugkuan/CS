package com.brightk.cs.core;

import androidx.annotation.NonNull;

public interface InterceptorCallback {
    void onNext(@NonNull UriRequest request);
    void onError(@NonNull  CsException e);
}
