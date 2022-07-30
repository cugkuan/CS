package com.demo.app;

import android.app.Application;
import android.util.Log;

import com.cugkuan.cs.CS;
import com.cugkuan.cs.core.CsUtils;
import com.cugkuan.cs.core.annotation.AutoRegisterTarget;
import com.demo.test2.TestService;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String key = CsUtils.getKey("app://app2/service1");
        CS.register(key, TestService.class);

        Log.e("lmk","注入代码");
        autoRegister();
    }


    @AutoRegisterTarget
    public void autoRegister(){
    }
}
