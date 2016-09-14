package com.qihoo.bumppic;

import android.os.FileObserver;
import android.util.Log;

import com.qihoo.bumppic.frgment.HotFragment;
import com.qihoo.bumppic.frgment.RecentFragment;
import com.qihoo.bumppic.utils.ToastUtils;


public class MainActivity extends SlidingBaseActivity{

    protected void initFragment() {
        recentF = new RecentFragment();
        hotf = new HotFragment();
        //saF = new SpecialAttentionFragment();
        lists.add(recentF);lists.add(hotf);//lists.add(saF);

    }

    @Override
    protected void setView() {
        super.setView();
    }

// private void setHomeMenuTextDrawableTop(TextView textView, Drawable drawable) {
//         if (textView == null || drawable == null) {
//             return;
//         }
//         textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
//         textView.invalidate();
//     }
}
