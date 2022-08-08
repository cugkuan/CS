package com.brightk.cs.core;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class UriRequest {

    private WeakReference<Context> context;
    private String action;
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

    public void setAction(@Nullable String action){
        this.action = action;
    }
    public @Nullable String getAction(){
        return action;
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



}
