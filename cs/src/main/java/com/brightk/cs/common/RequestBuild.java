package com.brightk.cs.common;

import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import com.brightk.cs.CS;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 请求参数构建
 */
public class RequestBuild {
    private Uri.Builder uriBuilder;
    private Uri uri;
    private String action;
    private Bundle params;
    private Context context;

    public RequestBuild(String url) {
        this.uri = Uri.parse(url);
    }

    public RequestBuild addQueryParams(String key, String value) {
        if (uriBuilder == null) {
            uriBuilder = uri.buildUpon();
        }
        uriBuilder.appendQueryParameter(key, value);
        return this;
    }

    public RequestBuild setContext(Context context) {
        this.context = context;
        return this;
    }

    public RequestBuild setAction(String action) {
        this.action = action;
        return this;
    }

    public RequestBuild setParams(Bundle params) {
        this.params = params;
        return this;
    }

    public RequestBuild addParams(String key, Object value) throws CsNotSupportParamsException {
        if (params == null) {
            params = new Bundle();
        }
        if (value instanceof Byte) {
            params.putByte(key, (Byte) value);
        } else if (value instanceof Character) {
            params.putChar(key, (Character) value);
        } else if (value instanceof Short) {
            params.putShort(key, (Short) value);
        } else if (value instanceof Float) {
            params.putFloat(key, (Float) value);
        } else if (value instanceof CharSequence) {
            params.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Parcelable) {
            params.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Size) {
            params.putSize(key, (Size) value);
        } else if (value instanceof SizeF) {
            params.putSizeF(key, (SizeF) value);
        } else if (value instanceof Parcelable[]) {
            params.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof ArrayList) {
            params.putParcelableArrayList(key, (ArrayList) value);
        } else if (value instanceof SparseArray) {
            params.putSparseParcelableArray(key, (SparseArray) value);
        } else if (value instanceof Serializable) {
            params.putSerializable(key, (Serializable) value);
        } else if (value instanceof byte[]) {
            params.putByteArray(key, (byte[]) value);
        } else if (value instanceof short[]) {
            params.putShortArray(key, (short[]) value);
        } else if (value instanceof char[]) {
            params.putCharArray(key, (char[]) value);
        } else if (value instanceof float[]) {
            params.putFloatArray(key, (float[]) value);
        } else if (value instanceof CharSequence[]) {
            params.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof Bundle) {
            params.putBundle(key, (Bundle) value);
        } else if (value instanceof Binder) {
            params.putBinder(key, (Binder) value);
        } else if (value instanceof IBinder) {
            params.putBinder(key, (IBinder) value);
        } else {
            throw new CsNotSupportParamsException(value);
        }
        return this;
    }

    public void connect(OnRequestResultListener listener) {
        UriRequest request;
        if (uriBuilder != null) {
            request = new UriRequest(uriBuilder.build());
        } else {
            request = new UriRequest(uri);
        }
        request.setAction(action);
        request.setParams(params);
        request.setContext(context);

        CS.startRequest(request, listener);
    }
}
