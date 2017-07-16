package com.oracle.schoolcircle.scrip.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.oracle.schoolcircle.http.parser.MessageLastParser;
import com.oracle.schoolcircle.manager.AppManager;
import com.oracle.schoolcircle.scrip.activity.ChatActivity;
import com.oracle.schoolcircle.scrip.adapter.MessageLVAdapter;
import com.oracle.schoolcircle.scrip.comparator.CDateAscComparator;
import com.oracle.schoolcircle.scrip.comparator.MDateDescComparator;
import com.oracle.schoolcircle.scrip.constants.ChatConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/10.
 */

public class MessageFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView lv_scrip_message;
    private List<MessageEntity> list;
    private MessageLVAdapter adapter;
    private ImageView iv_message_hidden;
    private TextView tv_message_hidden;
    //创建一个广播接收器，接收及时信息
    private MsgAcceptBroadcast mab = new MsgAcceptBroadcast();

    /**
     * 接收即时信息的广播接收器
     */
    private class MsgAcceptBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MessageEntity messageEntity = (MessageEntity) intent.getSerializableExtra("message");
            Log.d("mqtt", "消息界面接收到广播，接收到的内容为:" + messageEntity.getMessage_content());
            //得到发过来的用户id和目标用户id
            int target_user_id = messageEntity.getTarget_user_id();
            int user_id = messageEntity.getUser_id();
            Log.d("mqtt", "该消息的发送方向是: " + messageEntity.getUser_id() + "--->>>" + messageEntity.getTarget_user_id());

            if (adapter == null) {
                //当adapter为null的时候  说明没有数据
                list = new ArrayList<>();
                list.add(messageEntity);
                adapter = new MessageLVAdapter(getContext(), list);
                openHidden();
                lv_scrip_message.setAdapter(adapter);
            } else {
                for (int i = 0; i < adapter.getCount(); i++) {
                    MessageEntity m = (MessageEntity) adapter.getItem(i);
                    int mUserId = m.getUser_id();
                    int mTargetUserId = m.getTarget_user_id();
                    if (
                            mUserId == user_id && mTargetUserId == target_user_id
                                    || mUserId == target_user_id && mTargetUserId == user_id
                            ) {
                        Log.d("mqtt", "当前listview列表中已存在，正在更改listview视图");
                        //得到listview的子视图
                        View v = lv_scrip_message.getChildAt(i - lv_scrip_message.getFirstVisiblePosition());
                        if (v != null) {
                            /**
                             * 判断聊天界面是否打开
                             */
                            if (AppManager.getAppManager().isOpenActivity(ChatActivity.class)) {
                                /**
                                 * 聊天界面开着的时候  字体不为红  不播放音乐
                                 */
                            } else {
                                /**
                                 * 判断自己是发送方还是接收方来进行改变红色字体和播放音乐
                                 */
                                if (getContext() != null) {
                                    SharedPreferences sp = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                    int u_id = sp.getInt("user_id", 0);
                                    if (u_id != user_id) {
                                        TextView tv_scrip_message_content = (TextView) v.findViewById(R.id.tv_scrip_message_content);
                                        //设置字体颜色为红色
                                        tv_scrip_message_content.setTextColor(Color.RED);
                                        /**
                                         * 播放消息音乐,放入子线程
                                         */
                                        Log.d("mqtt", "放入子线程中播放消息音乐");
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                if (getContext() != null) {
                                                    playMedia();
                                                }
                                            }
                                        }.start();
                                    }
                                }


                            }

                        }
                        list.remove(i);
                        list.add(messageEntity);
                        Collections.sort(list, new MDateDescComparator());
                        adapter.notifyDataSetChanged();
                        break;
                    } else {
                        //判断是否遍历完
                        if (i == adapter.getCount() - 1) {
                            Log.d("mqtt", "遍历完成，当前listview列表中不存在存在，添加数据，告知listview刷新");
                            list.add(messageEntity);
                            adapter.notifyDataSetChanged();
                            /**
                             * 播放消息音乐,放入子线程
                             */
                            Log.d("mqtt", "放入子线程中播放消息音乐");
                            new Thread() {
                                @Override
                                public void run() {
                                    if (getContext() != null) {
                                        playMedia();
                                    }
                                }
                            }.start();
                        }
                    }

                }
            }
        }
    }

    /**
     * 播放音乐
     */
    private void playMedia() {
        MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.msg);
        mp.start();
        Log.d("mqtt", "播放音乐成功");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView() {
        lv_scrip_message = (ListView) rootView.findViewById(R.id.lv_scrip_message);
        iv_message_hidden = (ImageView) rootView.findViewById(R.id.iv_message_hidden);
        tv_message_hidden = (TextView) rootView.findViewById(R.id.tv_message_hidden);
        //请求数据
        requestData();
        //添加点击事件
        lv_scrip_message.setOnItemClickListener(this);
        //注册广播
        getContext().registerReceiver(mab, new IntentFilter(ChatConstants.CHAT_MESSAGE));
    }

    private void requestData() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "message");
        //得到用户id
        SharedPreferences sp = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        int user_id = sp.getInt("user_id", 0);
        map.put("user_id", user_id + "");
        MessageLastParser parser = new MessageLastParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.MESSAGE_CONTACT_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                MessageData messageData = (MessageData) beanData;
                if (messageData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
                    list = messageData.getList();
                    Collections.sort(list, new MDateDescComparator());
                    adapter = new MessageLVAdapter(getContext(), list);
                    openHidden();
                    lv_scrip_message.setAdapter(adapter);
                } else {
                    closeHidden();
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    public void openHidden() {
        iv_message_hidden.setVisibility(View.GONE);
        tv_message_hidden.setVisibility(View.GONE);
    }

    public void closeHidden() {
        iv_message_hidden.setVisibility(View.VISIBLE);
        tv_message_hidden.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv_scrip_message_content = (TextView) view.findViewById(R.id.tv_scrip_message_content);
        tv_scrip_message_content.setTextColor(getResources().getColor(R.color.text_color));
        MessageEntity message = list.get(position);
        //得到当前用户id
        SharedPreferences sp = getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        int user_id = sp.getInt("user_id", 0);
        int old_target_user_id = message.getTarget_user_id();
        int old_user_id = message.getUser_id();
        //得到目标用户id
        int target_user_id = getTargetUserId(user_id, old_target_user_id, old_user_id);
        String target_user_name = message.getUser_name();
        String target_user_img = message.getUser_img();
        //将用户id 目标用户id 目标用户姓名  目标用户头像地址传过去
        Bundle bundle = new Bundle();
        bundle.putInt("target_user_id", target_user_id);
        bundle.putInt("user_id", user_id);
        bundle.putString("target_user_name", target_user_name);
        bundle.putString("target_user_img", target_user_img);
        ChatActivity.actionStart(getContext(), bundle);
    }

    private int getTargetUserId(int user_id, int old_target_user_id, int old_user_id) {
        if (user_id == old_user_id) {
            return old_target_user_id;
        }
        if (user_id == old_target_user_id) {
            return old_user_id;
        }
        return 0;
    }
}
