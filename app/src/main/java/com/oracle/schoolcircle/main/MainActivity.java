package com.oracle.schoolcircle.main;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.content.fragment.SchoolFragment;
import com.oracle.schoolcircle.my.fragment.MyFragment;
import com.oracle.schoolcircle.news.fragment.NewsFragment;
import com.oracle.schoolcircle.scrip.fragment.ScripFragment;
import com.oracle.schoolcircle.push.PushService;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_main;
    private RadioButton btn_news;
    private RadioButton btn_content;
    private RadioButton btn_scrip;
    private RadioButton btn_my;
    //判断是否第一次加载界面
    private boolean isFirst = true;
    // mqtt
    public static final String TAG = "MainActivity";
    private String mDeviceID ;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        rg_main.setOnCheckedChangeListener(this);
        btn_news = (RadioButton) findViewById(R.id.rb_main_news);
        btn_content = (RadioButton) findViewById(R.id.rb_main_content);
        btn_scrip = (RadioButton) findViewById(R.id.rb_main_scrip);
        btn_my = (RadioButton) findViewById(R.id.rb_main_my);
        //设置UI
        configUI();
        //从sp中得到用户id
        mDeviceID = getUserId();
        Log.d("mqtt","mqtt的设备名为："+mDeviceID);
        //连接mqtt服务器
        startMqttServer();
    }

    private void startMqttServer() {
        SharedPreferences.Editor editor = getSharedPreferences(PushService.TAG,
                MODE_PRIVATE).edit();
        editor.putString(PushService.PREF_DEVICE_ID, mDeviceID);
        editor.commit();
//        PushService.actionStart(getApplicationContext());
        PushService.actionStart(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 界面不存在时断开mqtt
         */
        PushService.actionStop(this);
    }

    private String getUserId() {
        SharedPreferences sp = getSharedPreferences("userinfo",MODE_PRIVATE);
        int user_id = sp.getInt("user_id",0);
        return user_id+"";
    }

    /**
     * 初始化之后设置界面
     */
    private void configUI() {
        if (isFirst) {
            btn_news.setSelected(true);
            replaceFragment(new NewsFragment(), R.id.fl_fragment);
            isFirst = false;
        }

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    public void replaceFragment(Fragment fg, int id) {
        //得到碎片管理器
        FragmentManager fm = getSupportFragmentManager();
        //开启一个事物
        FragmentTransaction ft = fm.beginTransaction();
        //1、替换哪块地方(要替换的那个视图的id)  2、替换的值
        ft.replace(id, fg);
        //提交
        ft.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_main_news:
                replaceFragment(new NewsFragment(), R.id.fl_fragment);
                break;
            case R.id.rb_main_content:
                replaceFragment(new SchoolFragment(), R.id.fl_fragment);
                break;
            case R.id.rb_main_scrip:
                replaceFragment(new ScripFragment(), R.id.fl_fragment);
                break;
            case R.id.rb_main_my:
                replaceFragment(new MyFragment(), R.id.fl_fragment);
                break;
        }
    }
}
