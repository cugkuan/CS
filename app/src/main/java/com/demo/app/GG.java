package com.demo.app;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brightk.cs.core.CsService;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRequest;
import com.brightk.cs.core.annotation.CsUri;

@CsUri(uri = "app://app/service")
public class GG implements CsService {
    @Override
    public void call(@NonNull UriRequest uriRequest, @Nullable OnRequestResultListener listener) {
        Log.e("lmk","cdddagarafafafaasdccccccccc");
    }
}
