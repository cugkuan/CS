package com.demo.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Half;
import android.util.Log;
import android.widget.Toast;

import com.brightk.cs.CS;
import com.brightk.cs.common.UriRequestBuild;
import com.brightk.cs.core.UriRespond;
import com.demo.test1.Test1Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_click).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, Test1Activity.class);
            startActivity(intent);
        });
//        UriRespond respond = new UriRequestBuild( "app://app1/service2")
//                .connect();
//
//        String msg  = null;
//        if (respond.code == CS.CS_CODE_SUCCEED){
//            msg = respond.data.toString();
//        }else {
//            msg = respond.throwable.getMessage();
//        }
//        String finalMsg = msg;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, finalMsg,Toast.LENGTH_LONG).show();
//            }
//        });
        new Handler().postDelayed(() -> new Thread(() -> {
           UriRespond respond = new UriRequestBuild( "app://app1/service2")
                    .connect();
           String msg  = null;
           if (respond.code == CS.CS_CODE_SUCCEED){
               msg = respond.data.toString();
           }else {
               msg = respond.throwable.getMessage();
           }
            String finalMsg = msg;
            runOnUiThread(() -> Toast.makeText(MainActivity.this, finalMsg,Toast.LENGTH_LONG).show());
        }).start(),2000);

    }
}