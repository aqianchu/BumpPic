package com.qihoo.bumppic.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;
import com.qihoo.bumppic.utils.ToastUtils;

public class GeneralActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        setBackListener();
        ((TextView)findViewById(R.id.title_text)).setText("通用");
        setView();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.clean_buffer).setOnClickListener(this);
    }

    private void setView() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.clean_buffer:
                ToastUtils.showShort(this,"缓存已清理");
                break;
        }
    }
}
