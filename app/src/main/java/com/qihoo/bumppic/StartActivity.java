package com.qihoo.bumppic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends ActivityBase {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Intent intent = new Intent(StartActivity.this,SendActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_activity_start);

        startMain();
    }

    private void startMain() {
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.currentThread().sleep(2000);
                    handler.sendEmptyMessage(1);
                }catch (Exception e){
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
