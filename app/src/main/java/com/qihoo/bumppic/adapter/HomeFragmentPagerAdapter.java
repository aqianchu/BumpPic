package com.qihoo.bumppic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by hacker on 16/8/27.
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter{

    List<Fragment>lists;
    public HomeFragmentPagerAdapter(FragmentManager fm, List<Fragment>lists) {
        super(fm);
        this.lists = lists;
    }

    @Override
    public Fragment getItem(int position) {
        return lists.get(position);
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
