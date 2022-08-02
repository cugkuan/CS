package com.brightk.cs.core;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brightk.cs.CS;

import java.lang.ref.WeakReference;
import java.util.Map;

public class UriRequest {
    private WeakReference<Context> context;
    private Bundle params;
    private final @NonNull
    Uri uri;

    public UriRequest(Uri uri) {
        this(null, uri);
    }

    public UriRequest(@Nullable Context context, @NonNull Uri uri) {
        if (context != null) {
            this.context = new WeakReference(context);
        }
        this.uri = uri;
    }

    public @NonNull
    Uri getUri() {
        return uri;
    }

    public void setContext(Context context) {
        if (context == null) {
            this.context = null;
        } else {
            this.context = new WeakReference(context);
        }
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public @Nullable
    Bundle getParams() {
        return params;
    }

    public @Nullable
    Context getContext() {
        if (context != null) {
            return context.get();
        } else {
            return null;
        }
    }


    final public CsService getService() {
        String key = CsUtils.getKey(uri);
        return ComponentServiceManger.getService(key);
    }

    public UriRespond connect() {
        CsService csService = getService();
        if (csService != null) {
            return csService.call(this);
        } else {
            return new UriRespond(CS.CS_CODE_NOT_FIND);
        }
    }
}
