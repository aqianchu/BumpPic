package com.qihoo.bumppic.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;

public class ActivityRegister extends ActivityBase {

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
                startActivity(new Intent(this,ActivityLogin.class));
                finish();
                break;
        }
    }
}
