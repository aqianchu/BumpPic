package com.qihoo.bumppic.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;

public class UserActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setBackListener();
        setView();
        setListener();
    }

    private void setView() {

    }

    private void setListener() {
        findViewById(R.id.mycollect_relativelayout).setOnClickListener(this);
        findViewById(R.id.myfootprint_rl).setOnClickListener(this);
        findViewById(R.id.message_user_rl).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.mycollect_relativelayout:
                startActivity(new Intent(this,CollectActivity.class));
                break;
            case R.id.myfootprint_rl:
                startActivity(new Intent(this,FootprintActivity.class));
                break;
            case R.id.message_user_rl:
                startActivity(new Intent(this,MessageActivity.class));
                break;
            case R.id.mytag_user_rl:
                startActivity(new Intent(this,MyTagActivity.class));
                break;
        }
    }
}
