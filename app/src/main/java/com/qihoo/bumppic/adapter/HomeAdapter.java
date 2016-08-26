package com.qihoo.bumppic.adapter;

import android.content.Context;
import android.view.View;

import com.qihoo.bumppic.R;

import java.util.List;

/**
 * Created by hacker on 16/8/26.
 */
public class HomeAdapter extends CommonAdapter<Integer>{

    public HomeAdapter(Context context,List<Integer> datas) {
        super(context, datas, R.layout.home_item_layout);
    }

    @Override
    public void convert(ViewHolder holder, Integer integer) {
        View view = holder.getView(R.id.home_item_framelayout);
        view.setBackgroundResource(integer);
    }
}
