package com.demo.app;

import android.app.Application;

import com.cugkuan.cs.CS;
import com.cugkuan.cs.core.CsUtils;
import com.demo.test2.TestService;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String key = CsUtils.getKey("app://app2/service1");
        CS.register(key, TestService.class);
    }
}
