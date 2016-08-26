package com.qihoo.bumppic.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by hacker on 16/8/26.
 */
public class ToastUtils {
    public static void showLong(Context context, String msg){
        Toast.makeText((Activity)context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

}
