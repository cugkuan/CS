package com.demo.app;

import android.app.Application;
import com.brightk.cs.CS;
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CS.init();
    }

}
