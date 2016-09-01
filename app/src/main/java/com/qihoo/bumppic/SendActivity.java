package com.qihoo.bumppic;

import android.os.Bundle;
import android.widget.TextView;

import com.qihoo.bumppic.frgment.FilterFragment;
import com.qihoo.bumppic.frgment.PengFragment;

public class SendActivity extends SlidingBaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sethide(true);
        ((TextView)findViewById(R.id.home_title_social)).setText("碰一碰");
    }

    @Override
    protected void initFragment() {
        recentF = new PengFragment();
        hotf = new FilterFragment();
        lists.add(recentF);lists.add(hotf);
    }

}
