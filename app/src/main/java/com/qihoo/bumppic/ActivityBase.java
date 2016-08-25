package com.qihoo.bumppic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by hacker on 16/8/25.
 */
public class ActivityBase extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void setBackListener(){
        findViewById(R.id.back_bt).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_bt:
                finish();
                break;
        }
    }
}
