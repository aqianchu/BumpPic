package com.qihoo.bumppic.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;
import com.qihoo.bumppic.utils.ToastUtils;

public class SettingActivity  extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setBackListener();
        ((TextView)findViewById(R.id.title_text)).setText("设置");
        setView();
        setListener();
    }

    private void setView() {

    }

    private void setListener() {
        findViewById(R.id.setting_item_acountsafe_rl).setOnClickListener(this);
        findViewById(R.id.setting_item_notify_rl).setOnClickListener(this);
        findViewById(R.id.setting_item_general_rl).setOnClickListener(this);
        findViewById(R.id.setting_item_about_rl).setOnClickListener(this);
        findViewById(R.id.feedback_rl).setOnClickListener(this);
        findViewById(R.id.setting_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.setting_item_acountsafe_rl:
                Intent intent = new Intent(this,AcouuntSafeActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_item_notify_rl:
                Intent intent1 = new Intent(this,NotificationActivity.class);
                startActivity(intent1);
                break;
            case R.id.setting_item_general_rl:
                Intent intent2= new Intent(this,GeneralActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting_item_about_rl:
                Intent intent3= new Intent(this,AboutActivity.class);
                startActivity(intent3);
                break;
            case R.id.feedback_rl:
                Intent intent4= new Intent(this,FeedbackActivity.class);
                startActivity(intent4);
                break;
            case R.id.setting_logout:
//                Intent intent5= new Intent(this,FeedbackActivity.class);
//                startActivity(intent5);
                ToastUtils.showShort(this,"退出成功");
                break;
        }
    }
}
