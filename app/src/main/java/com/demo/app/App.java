package com.demo.app;

import android.app.Application;

import com.brightk.cs.CS;
import com.brightk.cs.common.UriRequestBuild;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CS.init();
        new UriRequestBuild("app://app/service2")
                .call();
    }

}
