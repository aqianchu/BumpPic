package com.qihoo.bumppic;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qihoo.bumppic.frgment.FilterFragment;
import com.qihoo.bumppic.frgment.PengFragment;
import com.qihoo.bumppic.utils.FileUtils;
import com.qihoo.bumppic.utils.ToastUtils;

public class SendActivity extends SlidingBaseActivity  {
    //String strs[] = new String[]{"SM-N9100:19","YQ601:19"};
    public static final int FILECHANGE_MSG = 9881;
    private FileObserver fileObserver;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FILECHANGE_MSG:
//                    String str = Build.MODEL+":"+Build.VERSION.SDK;
//                      boolean temp = false;
//                    str.equals("YQ601:19");
//                    for (int i=0;i<strs.length;i++){
//                        if (str.equals(strs[i])){
//                            temp = true;
//                        }
//                    }
//                    if (!temp){
//                        ToastUtils.showShort(mContext,"您的手机暂不支持碰一碰,我们稍后为你添加改功能");
//                        return;
//                    }
                    setCurrentPager(1);
                    setImgBeamSrc((String) msg.obj);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sethide(true);
        ((TextView)findViewById(R.id.home_title_social)).setText("碰一碰");
        findViewById(R.id.home_title_social).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("test", Build.MODEL+":"+Build.VERSION.SDK);
            }
        });

    }

    @Override
    protected void initFragment() {
        recentF = new PengFragment();
        hotf = new FilterFragment();
        lists.add(recentF);lists.add(hotf);
        String child="/beam/";
        String parentPath = Environment.getExternalStorageDirectory().getPath()+child;
        String str = Build.MODEL+":"+Build.VERSION.SDK;
        if (str.equals("YQ601:19")){
            child = "/beam/";
        }else{
            child = "/Download/";
            parentPath = "/storage/emulated/legacy/Download/";
//            parentPath = "/sdcard/Download/";
        }//  /storage/emulated/0/Download/


        Log.i("filepath",parentPath);
        fileObserver= FileUtils.fileObsever(parentPath,this);
        fileObserver.startWatching();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileObserver.stopWatching();
    }

    public void setCurrentPager(int position){
        Log.i("test","setCurrentPager");
        changeHomeSelected(position);
    }

    public void setImgBeamSrc(String path){
        Log.i("test","setImgBeamSrc");
        ((FilterFragment)hotf).setImgBeamSrc(path);
    }

}
