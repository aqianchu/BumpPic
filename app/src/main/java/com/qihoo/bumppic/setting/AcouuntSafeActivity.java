package com.qihoo.bumppic.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;

public class AcouuntSafeActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        setBackListener();
        ((TextView)findViewById(R.id.title_text)).setText("账号安全");
    }
}
