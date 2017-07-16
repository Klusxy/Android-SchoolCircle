package com.oracle.schoolcircle.content.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseFragment;
import com.oracle.schoolcircle.content.activity.ContentSendActivity;
import com.oracle.schoolcircle.entity.ContentType;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.ContentTypeData;
import com.oracle.schoolcircle.http.parser.ContentTypeParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/8.
 */

public class SchoolFragment extends BaseFragment implements ViewPager.OnPageChangeListener,View.OnClickListener {
    private SmartTabLayout viewPagerTab;
    private ViewPager viewPager = null;
    private FragmentPagerItemAdapter adapter;
    public static List<ContentType> list;
    private ContentFragment contentFragment;
    //发布内容按钮
    private ImageView iv_school_send;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(getClass().getSimpleName(), "handle收到消息，更新UI");
            if (adapter == null) {
                //为viewPagerAdapter添加碎片，创建适配器
                FragmentPagerItems.Creator fpi_c = FragmentPagerItems.with(getContext());
                for (ContentType type : list) {
                    String type_name = type.getType_name();
                    fpi_c.add(type_name, ContentFragment.class);
                }
                adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), fpi_c.create());
                viewPager.setAdapter(adapter);
                //添加监听
                viewPager.addOnPageChangeListener(SchoolFragment.this);
                //得到第一个碎片对象
                contentFragment = (ContentFragment) adapter.getPage(0);
                if (contentFragment != null) {
                    contentFragment.getType_id(list.get(0).getType_id());
                }
                //将ViewPager添加在smartTabLayout中
                viewPagerTab.setViewPager(viewPager);
            } else {

            }
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_school;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
        iv_school_send = (ImageView) rootView.findViewById(R.id.iv_school_send);
        iv_school_send.setOnClickListener(this);
        //请求类型数据
        requetTypeData();
    }

    private void requetTypeData() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "select");
        ContentTypeParser parser = new ContentTypeParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.CONTENT_TYPE_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                ContentTypeData contentTypeData = (ContentTypeData) beanData;
                if (contentTypeData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
                    //请求成功
                    list = contentTypeData.getList();
                    new Thread() {
                        @Override
                        public void run() {
                            //得到数据  通知主线程刷新UI
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                        }
                    }.start();
                } else {

//                    toast("请求数据失败");
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d("adada",position+"   scroll");
    }

    @Override
    public void onPageSelected(int position) {
        contentFragment = (ContentFragment) adapter.getPage(position);
        contentFragment.getType_id(list.get(position).getType_id());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             *发布内容按钮
             */
            case R.id.iv_school_send :
                if (list.size()!=0) {
                    ContentSendActivity.actionStart(getContext(),list);
                }else {
                    toast("界面未初始化完成");
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.d("adada",state+"   state");
    }

    public static int getTypeId(int positon) {
        return list.get(positon).getType_id();
    }

}
