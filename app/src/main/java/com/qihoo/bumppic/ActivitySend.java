package com.qihoo.bumppic;

import android.os.Bundle;

import com.qihoo.bumppic.frgment.HotFragment;

public class ActivitySend extends SlidingBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sethide(true);
    }

    @Override
    protected void initFragment() {
        recentF = new HotFragment();
        hotf = new HotFragment();
        lists.add(recentF);lists.add(hotf);
    }
}
