package com.qihoo.bumppic.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qihoo.bumppic.ActivityBase;
import com.qihoo.bumppic.R;
import com.qihoo.bumppic.adapter.FootPrintAdapter;
import com.qihoo.bumppic.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class FootprintActivity extends ActivityBase implements AdapterView.OnItemClickListener {

    private PullToRefreshListView ptrListView;
    private FootprintActivity mContext;
    List datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setData();
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_footprint);
        setBackListener();
        ((TextView)findViewById(R.id.title_text)).setText("我的足迹");
        setView();

    }
    int[] imgs = new int[]{R.drawable.background_picture1,R.drawable.background_picture3,R.drawable.background_picture4,
            R.drawable.background_picture6,R.drawable.background_picture8};

    private void setData(){
        datas = new ArrayList<Integer>();
        for (int i=0;i<imgs.length;i++){
            datas.add(imgs[i]);
        }
    }

    private void setView() {
        ptrListView = (PullToRefreshListView)findViewById(R.id.pulltofreshlistview_footprint);
        ptrListView.setAdapter(new FootPrintAdapter(mContext,datas));
        ptrListView.setMode(PullToRefreshBase.Mode.BOTH);
        ptrListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
