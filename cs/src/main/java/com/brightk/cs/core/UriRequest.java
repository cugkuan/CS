package com.brightk.cs.core;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class UriRequest {

    public static String REQUEST_PARAMS_KEY_ACTION = "uri_request_key_action";
    public static String REQUEST_PARAMS_KEY_BUNDLE = "uri_request_key_bundle";
    public static String REQUEST_PARAMS_DEFAULT_DATA = "uri_request_default_data";

    private WeakReference<Context> context;
    private Map<String, Object> params;
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

    public void setAction(@Nullable String action) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(REQUEST_PARAMS_KEY_ACTION, action);
    }

    public @Nullable
    String getAction() {
        return getStringParam(REQUEST_PARAMS_KEY_ACTION);
    }

    public void setData(Object data) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        params.put(REQUEST_PARAMS_DEFAULT_DATA, data);
    }

    public @Nullable
    Object getData() {
        if (params == null) {
            return null;
        } else {
            return params.get(REQUEST_PARAMS_DEFAULT_DATA);
        }
    }


    public void putParams(Map<String, Object> params) {
        if (this.params == null) {
            this.params = params;
        } else {
            this.params.putAll(params);
        }
    }

    public @Nullable
    Map<String, Object> getParams() {
        return params;
    }

    public void setBundle(Bundle bundle) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(REQUEST_PARAMS_KEY_BUNDLE, bundle);
    }

    public @Nullable
    Bundle getBundle() {
        return getParam(Bundle.class, REQUEST_PARAMS_KEY_BUNDLE);
    }

    public @Nullable
    Context getContext() {
        if (context != null) {
            return context.get();
        } else {
            return null;
        }
    }

    public @Nullable
    String getQueryParameter(String key) {
        return uri.getQueryParameter(key);
    }

    public @Nullable
    String getStringParam(String key) {
        return getParam(String.class, key);
    }

    public @Nullable
    Integer getIntParam(String key) {
        return getParam(Integer.class, key);
    }

    public @Nullable
    Long getLongParam(String key) {
        return getParam(Long.class, key);
    }

    public @Nullable
    Boolean getBooleanParam(String key) {
        return getParam(Boolean.class, key);
    }

    public @Nullable
    Object getParam(@Nullable String key) {
        if (params != null) {
            return params.get(key);
        } else {
            return null;
        }
    }


    public <T> T getParam(@NonNull Class<T> clazz, @NonNull String key) {
        if (params == null) {
            return null;
        }
        Object field = params.get(key);
        if (field != null) {
            try {
                return clazz.cast(field);
            } catch (ClassCastException e) {
            }
        }
        return null;
    }


}
