package com.demo.app;

import android.app.Application;
import android.util.Log;

import com.cugkuan.cs.CS;
import com.cugkuan.cs.core.CsUtils;
import com.cugkuan.cs.core.annotation.AutoRegisterTarget;
import com.demo.test2.TestService;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        autoRegister();
    }

    @AutoRegisterTarget
    public void autoRegister(){
    }
}
