package com.qihoo.bumppic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by hacker on 16/8/26.
 */
public abstract class CommonAdapter<T> extends BaseAdapter{
    private int layoutId;
    protected Context mContext;
    protected List<T>mDatas;
    protected LayoutInflater mInflater;
    protected View mParent;

    public CommonAdapter(Context context,List<T>datas,int layoutId){
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (mParent==null){
            mParent = viewGroup;
        }
        ViewHolder holder = ViewHolder.get(mContext,view,viewGroup,layoutId,i);
        convert(holder,(T)getItem(i));
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder,T t);
}
