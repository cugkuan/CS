package com.brightk.cs.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface CsService {

    void call(@NonNull UriRequest uriRequest,@Nullable OnRequestResultListener listener);

    default void cancel(@Nullable OnRequestResultListener listener){}
}
