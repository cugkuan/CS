package com.demo.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.cugkuan.cs.CS;

public class Test1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView tv = findViewById(R.id.tv_hello);


        String text = (String) (CS.startUri(this,"app://app2/service1").data);

        tv.setText(text);

    }
}