package com.qihoo.bumppic.setting;

import android.os.Bundle;
import android.widget.TextView;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;

public class FeedbackActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_back);
        setBackListener();
        ((TextView)findViewById(R.id.title_text)).setText("设置");
    }
}
