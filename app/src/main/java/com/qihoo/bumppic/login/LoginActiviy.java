package com.qihoo.bumppic.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;

public class LoginActiviy extends ActivityBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);
        setBackListener();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.login_register_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_register_tv:
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;
        }
    }
}
