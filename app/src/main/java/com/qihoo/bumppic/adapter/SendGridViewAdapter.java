package com.qihoo.bumppic.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.qihoo.bumppic.R;

import java.util.List;

/**
 * Created by hacker on 16/8/30.
 */
public class SendGridViewAdapter extends CommonAdapter<Integer>{
    public SendGridViewAdapter(Context context, List<Integer> datas) {
        super(context, datas, R.layout.send_item_layout);
    }

    @Override
    public void convert(ViewHolder holder, Integer s) {
        ImageView iv = holder.getView(R.id.send_item_ig);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        float density = dm.density;
        int item_width = (int)((width-10*density*4)/3);
        int item_height = (int)(item_width/0.638);
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                item_width,item_height);//传入自己需要的宽高
//        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                android.view.ViewGroup.LayoutParams.FILL_PARENT,
//                (int) mContext.getResources().getDimension(R.dimen.send_picture_height));//传入自己需要的宽高
        iv.setLayoutParams(param);
        iv.setImageResource(s);

    }
}
