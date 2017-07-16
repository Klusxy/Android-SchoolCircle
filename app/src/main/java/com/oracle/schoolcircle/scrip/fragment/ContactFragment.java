package com.oracle.schoolcircle.scrip.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseFragment;
import com.oracle.schoolcircle.entity.MessageEntity;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.MessageData;
import com.oracle.schoolcircle.http.parser.ContactParser;
import com.oracle.schoolcircle.http.parser.MessageLastParser;
import com.oracle.schoolcircle.scrip.adapter.ContactLVAdapter;
import com.oracle.schoolcircle.scrip.adapter.FriendAdapter;
import com.oracle.schoolcircle.scrip.adapter.MessageLVAdapter;
import com.oracle.schoolcircle.scrip.constants.ChatConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/10.
 */

public class ContactFragment extends BaseFragment {
    private ListView lv_scrip_contact;
    private List<MessageEntity> list;
    private ImageView iv_contact_hidden;
    private TextView tv_contact_hidden;
    private ContactLVAdapter adapter;
    /**
     * 判断是否第一次加载界面
     */
    private boolean isFirst = true;
    private FriendBroadcast friendBroadcast = new FriendBroadcast();

    private class FriendBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * 接收到即时消息  说明有新的联系人   重新请求数据
             */
            if (getContext() != null) {
                requestData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(friendBroadcast);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView() {
        lv_scrip_contact = (ListView) rootView.findViewById(R.id.lv_scrip_contact);
        iv_contact_hidden = (ImageView) rootView.findViewById(R.id.iv_contact_hidden);
        tv_contact_hidden = (TextView) rootView.findViewById(R.id.tv_contact_hidden);

        //请求数据
        requestData();
        //注册广播
        getContext().registerReceiver(friendBroadcast, new IntentFilter(ChatConstants.CHAT_FRIEND));
    }

    public void openHidden() {
        iv_contact_hidden.setVisibility(View.GONE);
        tv_contact_hidden.setVisibility(View.GONE);
    }

    public void closeHidden() {
        iv_contact_hidden.setVisibility(View.VISIBLE);
        tv_contact_hidden.setVisibility(View.VISIBLE);
    }

    private void requestData() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "contact");
        //得到用户id

        SharedPreferences sp = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        int user_id = sp.getInt("user_id", 0);
        map.put("user_id", user_id + "");

        ContactParser parser = new ContactParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.MESSAGE_CONTACT_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                MessageData messageData = (MessageData) beanData;
                if (messageData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
                    list = messageData.getList();
                    if (isFirst) {
                        adapter = new ContactLVAdapter(getContext(), list);
                        lv_scrip_contact.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    /**
                     * 判断有没有好友
                     */
                    if (list.size() != 0) {
                        openHidden();
                    } else {
                        closeHidden();
                    }
                } else {
                    closeHidden();
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }
}
