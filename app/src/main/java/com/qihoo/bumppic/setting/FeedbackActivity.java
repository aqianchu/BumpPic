package com.qihoo.bumppic.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;
import com.qihoo.bumppic.utils.ToastUtils;

public class FeedbackActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_back);
        setBackListener();
        ((TextView)findViewById(R.id.title_text)).setText("设置");
        findViewById(R.id.title_right_bt).setVisibility(View.VISIBLE);
        findViewById(R.id.title_right_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.title_right_bt:
                ToastUtils.showShort(this,"发送成功");
                break;
        }
    }
}
