package com.qihoo.bumppic.utils;

import android.content.Context;
import android.nfc.NfcAdapter;

/**
 * Created by hacker on 16/9/1.
 * https://github.com/kesenhoo/android-training-course-in-chinese/blob/master/content-sharing/beam-files/sending-files.md
 */
public class NfcUtils {
    public static String NFC_HINT = "您的手机不支持NFC,暂时无法使用碰一碰功能";
    public static boolean isSupportNfc(Context context){
        if (NfcAdapter.getDefaultAdapter(context)==null)
            return false;
        return NfcAdapter.getDefaultAdapter(context).isEnabled();
    }
}
