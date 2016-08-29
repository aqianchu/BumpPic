package com.qihoo.bumppic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qihoo.bumppic.adapter.HomeAdapter;
import com.qihoo.bumppic.adapter.HomeFragmentPagerAdapter;
import com.qihoo.bumppic.frgment.HotFragment;
import com.qihoo.bumppic.frgment.RecentFragment;
import com.qihoo.bumppic.frgment.SpecialAttentionFragment;
import com.qihoo.bumppic.login.ActivityLogin;
import com.qihoo.bumppic.login.ActivityRegister;
import com.qihoo.bumppic.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private SlidingMenu menu;
    private RadioGroup radiogroup;
    private RadioButton recent_rbt;
    private RadioButton hot_rbt;
    private RadioButton sa_rbt;
    private FragmentPagerAdapter fpAdapter;
    private ViewPager mViewPage;
    private HotFragment hotf;
    private RecentFragment recentF;
    private SpecialAttentionFragment saF;
    private List<Fragment>lists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSliding();
        initFragment();
        setView();
        setListener();
    }

    private void initFragment() {
        lists = new ArrayList<Fragment>();
        recentF = new RecentFragment();
        hotf = new HotFragment();
        saF = new SpecialAttentionFragment();
        lists.add(recentF);lists.add(hotf);lists.add(saF);
    }



    private void setView() {
        radiogroup = (RadioGroup)findViewById(R.id.radiogroup_home);
        recent_rbt = (RadioButton)findViewById(R.id.radiobt_home_recent);
        hot_rbt = (RadioButton)findViewById(R.id.radiobt_home_hot);
        sa_rbt = (RadioButton)findViewById(R.id.radiobt_home_specialattention);
        mViewPage = (ViewPager)findViewById(R.id.viewpager_home);
        mViewPage.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager(),lists));
        mViewPage.setCurrentItem(0);
        mViewPage.setOnPageChangeListener(this);
    }

    private void setListener() {
        //s胜((TextView)findViewById(R.id.home_title_social)).getPaint().setFakeBoldText(true);
        findViewById(R.id.home_linearlayout).setOnClickListener(this);
        findViewById(R.id.recommended_linearlayout).setOnClickListener(this);
        findViewById(R.id.toggle_sliding).setOnClickListener(this);
        findViewById(R.id.sliding_portarit).setOnClickListener(this);
        radiogroup.setOnCheckedChangeListener(this);
    }

    private void setSliding() {
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        //menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeEnabled(true);
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.layout_left_menu);
    }

    long last;
    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now-last<1500){
            finish();
        }else {
            last = now;
            ToastUtils.showShort(this,"再按一次退出~");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_linearlayout:
                Intent intent = new Intent(this, ActivityLogin.class);
                startActivity(intent);
                break;
            case R.id.recommended_linearlayout:
                startActivity(new Intent(this, ActivityRegister.class));
                break;
            case R.id.toggle_sliding:
                menu.toggle();
                break;
            case R.id.sliding_portarit:
                startActivity(new Intent(this,ActivityUser.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        changeHomeSelected(i);
    }
    private void changeHomeSelected(int i){
        switch (i){
            case R.id.radiobt_home_recent:
                recent_rbt.setChecked(true);
                hot_rbt.setChecked(false);
                sa_rbt.setChecked(false);
                mViewPage.setCurrentItem(0);
                break;
            case R.id.radiobt_home_hot:
                recent_rbt.setChecked(false);
                hot_rbt.setChecked(true);
                sa_rbt.setChecked(false);
                mViewPage.setCurrentItem(1);
                break;
            case R.id.radiobt_home_specialattention:
                recent_rbt.setChecked(false);
                hot_rbt.setChecked(false);
                sa_rbt.setChecked(true);
                mViewPage.setCurrentItem(2);
                break;
            case 0:
                recent_rbt.setChecked(true);
                hot_rbt.setChecked(false);
                sa_rbt.setChecked(false);
                break;
            case 1:
                recent_rbt.setChecked(false);
                hot_rbt.setChecked(true);
                sa_rbt.setChecked(false);
                break;
            case 2:
                recent_rbt.setChecked(false);
                hot_rbt.setChecked(false);
                sa_rbt.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeHomeSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
