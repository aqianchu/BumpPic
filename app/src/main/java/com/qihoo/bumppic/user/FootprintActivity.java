package com.qihoo.bumppic.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;

public class FootprintActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        setBackListener();
        ((TextView)findViewById(R.id.title_text)).setText("我的足迹");
    }
}
