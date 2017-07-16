package com.oracle.schoolcircle.scrip.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseFragment;
import com.oracle.schoolcircle.scrip.activity.RecommendActivity;
import com.oracle.schoolcircle.scrip.adapter.MsgAndContactAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/8.
 */

public class ScripFragment extends BaseFragment implements View.OnClickListener{

    private ViewPager vp_scrip;
    private TabLayout tl_scrip;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mFragmentList = new ArrayList<>();
    private Button btn_scrip_add;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_scrip;
    }

    @Override
    protected void initView() {
        tl_scrip = (TabLayout) rootView.findViewById(R.id.tl_scrip);
        btn_scrip_add = (Button) rootView.findViewById(R.id.btn_scrip_add);
        btn_scrip_add.setOnClickListener(this);
        mTitleList.add("消息");
        mTitleList.add("联系人");
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new ContactFragment());
        MsgAndContactAdapter adapter = new MsgAndContactAdapter(getChildFragmentManager(),mFragmentList,mTitleList);
        vp_scrip = (ViewPager) rootView.findViewById(R.id.vp_scrip);
        vp_scrip.setAdapter(adapter);
        tl_scrip.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tl_scrip.addTab(tl_scrip.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        tl_scrip.addTab(tl_scrip.newTab().setText(mTitleList.get(1)));
        tl_scrip.setupWithViewPager(vp_scrip);//将TabLayout和ViewPager关联起来。
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scrip_add:
                RecommendActivity.actionStart(getContext());
                break;
        }
    }
}
