package com.qihoo.bumppic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qihoo.bumppic.adapter.HomeFragmentPagerAdapter;
import com.qihoo.bumppic.login.ActivityLogin;
import com.qihoo.bumppic.login.ActivityRegister;
import com.qihoo.bumppic.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hacker on 16/8/29.
 */
public abstract class SlidingBaseActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    protected SlidingMenu menu;
    protected RadioGroup radiogroup;
    protected RadioButton recent_rbt;
    protected RadioButton hot_rbt;
    protected RadioButton sa_rbt;
    protected FragmentPagerAdapter fpAdapter;
    protected ViewPager mViewPage;
    protected Fragment hotf;
    protected Fragment recentF;
    protected Fragment saF;
    protected List<Fragment> lists;
    protected Button search_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lists = new ArrayList<Fragment>();
        initFragment();
        setContentView(R.layout.activity_main);
        setSliding();
        setView();
        setListener();
    }

    protected void setSliding() {
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

    protected void setView() {
        radiogroup = (RadioGroup)findViewById(R.id.radiogroup_home);
        recent_rbt = (RadioButton)findViewById(R.id.radiobt_home_recent);
        hot_rbt = (RadioButton)findViewById(R.id.radiobt_home_hot);
        sa_rbt = (RadioButton)findViewById(R.id.radiobt_home_specialattention);
        sa_rbt.setVisibility(View.GONE);
        mViewPage = (ViewPager)findViewById(R.id.viewpager_home);
        search_bt = (Button)findViewById(R.id.search_social);
        mViewPage.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager(),lists));
        mViewPage.setCurrentItem(0);
        mViewPage.setOnPageChangeListener(this);
    }

    /**
     * 是否是发送界面
     * @param isHide true是
     */
    protected void sethide(boolean isHide){
        if (isHide){
            recent_rbt.setText("发送");
            hot_rbt.setText("滤镜");
            sa_rbt.setVisibility(View.GONE);
            search_bt.setVisibility(View.GONE);
        }else {
            recent_rbt.setText("最近");
            hot_rbt.setText("热门");
            sa_rbt.setVisibility(View.VISIBLE);
            search_bt.setVisibility(View.VISIBLE);
        }
    }

    protected void setListener() {
        //s胜((TextView)findViewById(R.id.home_title_social)).getPaint().setFakeBoldText(true);
        findViewById(R.id.home_linearlayout).setOnClickListener(this);
        findViewById(R.id.recommended_linearlayout).setOnClickListener(this);
        findViewById(R.id.socail_sliding_relativelayout).setOnClickListener(this);
        findViewById(R.id.toggle_sliding).setOnClickListener(this);
        findViewById(R.id.sliding_portarit).setOnClickListener(this);
        search_bt.setOnClickListener(this);
        radiogroup.setOnCheckedChangeListener(this);
        findViewById(R.id.sliding_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_linearlayout:
                if (this instanceof ActivitySend){
                    menu.toggle();
                    return;
                }
                Intent intent = new Intent(this, ActivitySend.class);
                startActivity(intent);
                finish();
                break;
            case R.id.socail_sliding_relativelayout:
                if (this instanceof MainActivity) {
                    menu.toggle();
                    return;
                }
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.recommended_linearlayout:
                startActivity(new Intent(this, ActivityRegister.class));
                menu.toggle();
                break;
            case R.id.toggle_sliding:
                menu.toggle();
                break;
            case R.id.sliding_portarit:
                startActivity(new Intent(this,ActivityUser.class));
                menu.toggle();
                break;
            case R.id.search_social:
                startActivity(new Intent(this,ActivitySearch.class));
                menu.toggle();
                break;
            case R.id.sliding_send:
                startActivity(new Intent(this,ActivitySend.class));
                menu.toggle();
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

    protected abstract void initFragment();
}
