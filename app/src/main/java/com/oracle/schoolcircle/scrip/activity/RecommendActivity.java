package com.oracle.schoolcircle.scrip.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.FriendData;
import com.oracle.schoolcircle.http.parser.FriendParser;
import com.oracle.schoolcircle.scrip.adapter.FriendAdapter;
import com.oracle.schoolcircle.scrip.constants.ChatConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/15.
 */

public class RecommendActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ImageView iv_friend_back;
    private ListView lv_friend;
    private FriendAdapter adapter;
    private List<User> list;
    private FriendBroadcast friendBroadcast = new FriendBroadcast();
    private class FriendBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int user_id = intent.getIntExtra("user_id",0);
            int target_user_id = intent.getIntExtra("friend_id",0);
//            String message_content = "我添加你为联系人啦~";
            Log.d("mqtt","主页面接收到广播，得到的数据有:"+user_id+" --->  "+target_user_id);
            Map<String,Object> map = new HashMap<>();
            map.put("user_id",user_id+"");
            map.put("target_user_id",target_user_id+"");
            map.put("message_content","我添加你为联系人啦~");
            HttpServer.sendPostRequest(map,null,URLConstants.BASE_URL + URLConstants.SEND_CHAT_URL,null);
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_recommend;
    }

    @Override
    public void initView() {
        iv_friend_back = (ImageView) findViewById(R.id.iv_friend_back);
        iv_friend_back.setOnClickListener(this);
        lv_friend = (ListView) findViewById(R.id.lv_friend);
        lv_friend.setOnItemClickListener(this);
        requestData();
        //注册广播
        registerReceiver(friendBroadcast,new IntentFilter(ChatConstants.CHAT_FRIEND));
    }

    /**
     * 请求数据
     */
    private void requestData() {
        Map<String,Object> map = new HashMap<>();
        map.put("type","select");
        SharedPreferences sp = getSharedPreferences("userinfo",MODE_PRIVATE);
        int user_id = sp.getInt("user_id",0);
        map.put("user_id",user_id+"");
        FriendParser parser = new FriendParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.FRIEND_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                FriendData friendData = (FriendData) beanData;
                if (friendData.getFlag()== StatusCode.Dao.SELECT_SUCCESS) {
                    list = friendData.getList();
                    adapter = new FriendAdapter(RecommendActivity.this,list);
                    lv_friend.setAdapter(adapter);
                }else {
                    toast("获取数据失败");
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(friendBroadcast);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context,RecommendActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_friend_back :
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toast("item");
    }
}
