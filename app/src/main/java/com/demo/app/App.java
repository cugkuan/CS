package com.demo.app;

import android.app.Application;

import com.brightk.cs.CS;
import com.example.annotation.Builder;

@Builder
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CS.init();
    }

}
