package com.qihoo.bumppic;

import com.qihoo.bumppic.frgment.HotFragment;
import com.qihoo.bumppic.frgment.RecentFragment;
import com.qihoo.bumppic.frgment.SpecialAttentionFragment;


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
}
