package com.qihoo.bumppic.adapter;

import android.content.Context;
import android.view.View;

import com.qihoo.bumppic.R;

import java.util.List;

/**
 * Created by hacker on 16/8/26.
 */
public class FootPrintAdapter extends CommonAdapter<Integer>{

    public FootPrintAdapter(Context context, List<Integer> datas) {
        super(context, datas, R.layout.footprint_item_layout);
    }

    @Override
    public void convert(ViewHolder holder, Integer integer) {
//        View view = holder.getView(R.id.home_item_framelayout);
//        view.setBackgroundResource(integer);
    }
}
