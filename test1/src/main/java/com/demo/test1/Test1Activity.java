package com.demo.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.brightk.cs.CS;
import com.brightk.cs.common.UriRequestBuild;
import com.brightk.cs.core.OnRequestResultListener;
import com.brightk.cs.core.UriRespond;

public class Test1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView tv = findViewById(R.id.tv_hello);


       String s= (String) new UriRequestBuild("app://app2/service1").connect().data;

       tv.setText(s);



    }
}