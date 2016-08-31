package com.qihoo.bumppic.utils;

import android.os.Handler;

/**
 * Created by hacker on 16/8/30.
 */
public class ThreadUtils {
    public static final int THREAD_FIVE_MESSAGE_WHAT=100;
    public static void threadFiveMinute(final Handler handler){
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.currentThread().sleep(5000);
                    handler.sendEmptyMessage(THREAD_FIVE_MESSAGE_WHAT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
