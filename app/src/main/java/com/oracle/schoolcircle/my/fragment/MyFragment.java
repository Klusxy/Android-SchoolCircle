package com.oracle.schoolcircle.my.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseFragment;
import com.oracle.schoolcircle.broadcast.BroConstants;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.my.activity.DetailMyActivity;
import com.oracle.schoolcircle.my.adapter.MyLVAdapter;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/8.
 */

public class MyFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private ListView lv_my;
    private List<Integer> picIdList;
    private List<String> titleList;
    private int user_id;
    private MyLVAdapter adapter;
    //更新name的广播
    private UpdateUserBroadcast updateUserBroadcast = new UpdateUserBroadcast();
    //更新头像广播
    private UpdateUserImgBroadcast updateUserImgBroadcast = new UpdateUserImgBroadcast();
    /**
     * 更新img的广播
     */
    private class UpdateUserImgBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            View v = lv_my.getChildAt(1);
            MyLVAdapter.ViewHolder vh = (MyLVAdapter.ViewHolder) v.getTag();
            vh.iv_my_ic = (ImageView) v.findViewById(R.id.iv_my_ic);
            String user_img = intent.getStringExtra("user_img");
            ImageUtils.getBitmapUtils(getContext()).display(vh.iv_my_ic, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    Bitmap circle = ImageUtils.circleBitmap(bitmap);
                    imageView.setImageBitmap(circle);
                }

                @Override
                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

                }
            });
        }
    }
    /**
     * 更新name的广播
     */
    private class UpdateUserBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            View v = lv_my.getChildAt(1);
            MyLVAdapter.ViewHolder vh = (MyLVAdapter.ViewHolder) v.getTag();
            vh.tv_my_title = (TextView) v.findViewById(R.id.tv_my_title);
            SharedPreferences sp = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            String user_name = sp.getString("user_name","");
            vh.tv_my_title.setText(user_name);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        lv_my = (ListView) rootView.findViewById(R.id.lv_my);
        setData(); // 设置数据
        // 配置适配器
        adapter = new MyLVAdapter(getContext(), picIdList, titleList); // 布局里的控件id
        // 添加并且显示
        lv_my.setAdapter(adapter);
        //获取user_id
        getUserIdFromSP();
        lv_my.setOnItemClickListener(this);
        //注册name广播
        getContext().registerReceiver(updateUserBroadcast,new IntentFilter(BroConstants.UPDATE_USER));
        //注册img广播
        getContext().registerReceiver(updateUserImgBroadcast,new IntentFilter(BroConstants.UPDATE_USER_IMG));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(updateUserBroadcast);
        getContext().unregisterReceiver(updateUserImgBroadcast);
    }

    private void getUserIdFromSP() {
        SharedPreferences sp = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        user_id = sp.getInt("user_id",0);
    }


    private void setData() {
        // 组织数据源
        picIdList = new ArrayList<>();
        titleList = new ArrayList<>();
        picIdList.add(0);
        picIdList.add(0);
        picIdList.add(0);
        picIdList.add(R.drawable.ic_mine_who_look_me);
        picIdList.add(0);
        picIdList.add(R.drawable.ic_mine_themes);
        picIdList.add(R.drawable.ic_mine_reply);
        picIdList.add(R.drawable.ic_mine_collect);
        picIdList.add(R.drawable.ic_mine_photos);
        picIdList.add(0);
        picIdList.add(R.drawable.ic_mine_setting);
        titleList.add("");
        titleList.add("");
        titleList.add("");
        titleList.add("谁看过我");
        titleList.add("");
        titleList.add("我的主题");
        titleList.add("我的回复");
        titleList.add("我的收藏");
        titleList.add("我的相册");
        titleList.add("");
        titleList.add("设置");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //个人中心
        if (position==1) {
            DetailMyActivity.actionStart(getContext(),user_id);
        }else {
            toast("功能未开放");
        }
    }
}
