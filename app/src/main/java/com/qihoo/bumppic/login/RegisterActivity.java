package com.qihoo.bumppic.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;

public class RegisterActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_register);
        setBackListener();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.register_login_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_login_tv:
                startActivity(new Intent(this,LoginActiviy.class));
                finish();
                break;
        }
    }
}
