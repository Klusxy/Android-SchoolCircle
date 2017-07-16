package com.oracle.schoolcircle.scrip.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.entity.Chat;
import com.oracle.schoolcircle.entity.MessageEntity;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.ChatData;
import com.oracle.schoolcircle.http.parser.ChatParser;
import com.oracle.schoolcircle.scrip.adapter.ChatAdapter;
import com.oracle.schoolcircle.scrip.comparator.CDateAscComparator;
import com.oracle.schoolcircle.scrip.constants.ChatConstants;
import com.oracle.schoolcircle.scrip.voice.SoundMeter;
import com.oracle.schoolcircle.utils.KeyBoardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private ImageView iv_chat_back;
    private TextView tv_chat_title;
    private Button btn_chat_send;
    private EditText et_chat_content;

    /***
     * 传过来的数据和从sp当中获取到的数据
     * 目标用户id  name  img
     * 自己的id name img
     */
    private String target_user_name ;
    private int target_user_id;
    private String target_user_img;
    private int user_id;
    private ChatAdapter adapter;
    /**
     * 传给adapter的数据
     */
    private ListView lv_chat;
    private List<Chat> list;
    //创建一个广播接收器，接收及时信息
    private MsgAcceptBroadcast mab = new MsgAcceptBroadcast();
    /**
     * 语音和表情按钮
     */
    private ImageView iv_chat_voice;
    private ImageView iv_chat_face;


    /**
     * 语音方面
     */
    private boolean btn_vocie = false;
    //按住说话文字
    private TextView mBtnRcd;
    //语音表情按钮右边的总体布局
    private RelativeLayout rl_chat_bottom;

    //chat布局中的包裹整个录音界面层的布局
    private View rcChat_popup;
    //录音界面层控件
    private LinearLayout del_re;
    // ------------------------>>> 未知
    private int flag = 1;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort;
    //判断录音时间是否过短
    private boolean isShosrt = false;
    private Handler mHandler = new Handler();
    private ImageView voice_img1, voice_sc_img1;
    //记录开始录音时间和结束录音时间
    private long startVoiceT, endVoiceT;
    //录音文件名
    private String voiceName;
    private SoundMeter mSensor;
    //录音时动画
    private ImageView  voice_volume;

    /**
     * 接收即时信息的广播接收器
     */
    private class MsgAcceptBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MessageEntity messageEntity = (MessageEntity) intent.getSerializableExtra("message");
            Log.d("mqtt","消息界面接收到广播，接收到的内容为:"+messageEntity.getMessage_content());
            //得到发过来的用户id和目标用户id
            Log.d("mqtt","该消息的发送方向是: "+messageEntity.getUser_id()+"--->>>"+messageEntity.getTarget_user_id());
            //判断是接收方还是发送方
            int mUserId = messageEntity.getUser_id();
            int mTargetUserId = messageEntity.getTarget_user_id();
            //创建一个Chat实体类
            Chat c = new Chat();
            c.setMessage_content(messageEntity.getMessage_content());
            c.setMessage_date(messageEntity.getMessage_date());
            c.setTime(messageEntity.getVoice_time());
            if (user_id==mUserId) {
                //发送方  取出自己的name和img
                c.setType(Chat.MSG_TYPE_SEND);
                SharedPreferences sp = getSharedPreferences("userinfo",MODE_PRIVATE);
                String user_name = sp.getString("user_name","");
                String user_img = sp.getString("user_img","");
                c.setUser_img(user_img);
                c.setUser_name(user_name);
            }
            if (user_id==mTargetUserId) {
                //接收方
                c.setType(Chat.MSG_TYPE_RECEIVED);
                c.setUser_img(messageEntity.getUser_img());
                c.setUser_name(messageEntity.getUser_name());
            }
            list.add(c);
            adapter.notifyDataSetChanged();
            lv_chat.setSelection(adapter.getCount()-1);
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        iv_chat_back = (ImageView) findViewById(R.id.iv_chat_back);
        tv_chat_title = (TextView) findViewById(R.id.tv_chat_title);
        lv_chat = (ListView) findViewById(R.id.lv_chat);
        /**
         * 语音方面
         */
        iv_chat_voice = (ImageView) findViewById(R.id.iv_chat_voice);
        iv_chat_voice.setOnClickListener(this);
        //按住说话文字
        mBtnRcd = (TextView) findViewById(R.id.btn_rcd);
        //语音表情按钮右边的总体布局
        rl_chat_bottom = (RelativeLayout) findViewById(R.id.rl_chat_bottom);
        //录音界面中的控件
        del_re = (LinearLayout) findViewById(R.id.voice_del_re);
        rcChat_popup = this.findViewById(R.id.rcChat_popup);
        voice_rcd_hint_rcding = (LinearLayout)findViewById(R.id.voice_rcd_hint_rcding);
        voice_rcd_hint_loading = (LinearLayout)findViewById(R.id.voice_rcd_hint_loading);
        voice_rcd_hint_tooshort = (LinearLayout)findViewById(R.id.voice_rcd_hint_tooshort);
        voice_img1 = (ImageView)findViewById(R.id.voice_img1);
        voice_sc_img1 = (ImageView)findViewById(R.id.voice_sc_img1);
        mSensor = new SoundMeter();
        voice_volume = (ImageView) this.findViewById(R.id.voice_volume);
        /**
         * 给“按住说话”设置触摸事件
         */
        mBtnRcd.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                //按下语音录制按钮时返回false执行父类OnTouch
                return false;
            }
        });

        /**
         * 表情方面
         */
//        iv_chat_face = (ImageView) findViewById(R.id.iv_chat_face);
//        iv_chat_face.setOnClickListener(this);
        //给listview设置触摸事件隐藏软键盘
        lv_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardUtils.closeKeybord(et_chat_content,ChatActivity.this);
                return false;
            }
        });
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        btn_chat_send.setOnClickListener(this);
        et_chat_content = (EditText) findViewById(R.id.et_chat_content);
        iv_chat_back.setOnClickListener(this);
        //得到传过来的数据
        getBundleData();
        //请求数据
        requestData();
        //更新标题
        tv_chat_title.setText(target_user_name);
        //注册广播
        registerReceiver(mab,new IntentFilter(ChatConstants.CHAT_MESSAGE));
    }

    private void getBundleData() {
        //先从bundle中取出target_user_id和user_id  name  img
        //目标用户的id  姓名  头像
        Bundle bundle = getIntent().getExtras();
        target_user_id = bundle.getInt("target_user_id");
        target_user_name = bundle.getString("target_user_name");
        target_user_img =  bundle.getString("target_user_img");
        user_id = bundle.getInt("user_id");
    }

    public static void actionStart(Context context,Bundle bundle){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void requestData() {

        Map<String,Object> map = new HashMap<>();
        map.put("user_id",user_id+"");
        map.put("target_user_id",target_user_id+"");
        ChatParser parser = new ChatParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.CHAT_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                ChatData chatData = (ChatData) beanData;
                if (chatData.getFlag()== StatusCode.Dao.SELECT_SUCCESS) {
                    list = new ArrayList<Chat>();
                    SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    String user_name = sp.getString("user_name","");
                    String user_img = sp.getString("user_img","");
                    Log.d(TAG,"从sp中获取到当前用户的id为:"+user_id);
                    for(Chat c : chatData.getList()){
                        Log.d(TAG,"返回的数据：user_id"+c.getUser_id());
                        int mUser_id = c.getUser_id();
                        if (user_id==mUser_id) {
                            c.setType(Chat.MSG_TYPE_SEND);
                            c.setUser_name(user_name);
                            c.setUser_img(user_img);
                        }
                        if (user_id!=mUser_id) {
                            c.setType(Chat.MSG_TYPE_RECEIVED);
                            c.setUser_name(target_user_name);
                            c.setUser_img(target_user_img);
                        }
                        list.add(c);
                    }
                    /**
                     * 对list进行排序
                     */
                    CDateAscComparator comparator = new CDateAscComparator();
                    Collections.sort(list,comparator);
                    for (Chat chat : list) {
                        Log.d(TAG,"发送方："+chat.getUser_name()+"  聊天内容："+chat.getMessage_content()+"  消息类型："+chat.getType()+" 发送日期（升序）："+chat.getMessage_date());
                    }
                    adapter = new ChatAdapter(ChatActivity.this,list);
                    lv_chat.setAdapter(adapter);
                    lv_chat.setSelection(adapter.getCount()-1);
                }else {
                    toast("请求失败");
                }
            }

            @Override
            public void failure(String error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_chat_back :
                finish();
                break;
            /**
             * 发送数据
             */
            case R.id.btn_chat_send :
               if (et_chat_content.getText()==null) {
                    toast("输入框不能为空");
                }else {
                    //发送数据
                    String message_content = et_chat_content.getText().toString();
                    Map<String,Object> map = new HashMap<>();
                    map.put("user_id",user_id+"");
                    map.put("target_user_id",target_user_id+"");
                    map.put("message_content",message_content);
                    HttpServer.sendPostRequest(map,null,URLConstants.BASE_URL + URLConstants.SEND_CHAT_URL,null);
                    //发送完之后清空编辑框
                    et_chat_content.setText("");
                }
                break;
            /**
             * 语音文字切换按钮
             */
            case R.id.iv_chat_voice :
                if (btn_vocie) {
                    /**
                     * 如果不是语音，“按住说话”隐藏  编辑框和发送按钮显示
                     * 将btn_voice置为录音状态
                     *
                     *  将按钮图片换成文字图片------------->>>>
                     */
                    mBtnRcd.setVisibility(View.GONE);
                    rl_chat_bottom.setVisibility(View.VISIBLE);
                    btn_vocie = false;
                    iv_chat_voice.setImageResource(R.drawable.chatting_setmode_msg_btn);

                } else {
                    mBtnRcd.setVisibility(View.VISIBLE);
                    rl_chat_bottom.setVisibility(View.GONE);
                    iv_chat_voice.setImageResource(R.drawable.chatting_setmode_voice_btn);
                    btn_vocie = true;
                }
                break;
            /**
             * 表情按钮
             */
//            case R.id.iv_chat_face :
//
//                break;
        }
    }

    /**
     * 按下“按住说话”时进行语音录制
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!Environment.getExternalStorageDirectory().exists()) {
            Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
            return false;
        }

        if (btn_vocie) {
            System.out.println("1");
            int[] location = new int[2];
            mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
            int btn_rc_Y = location[1];
            int btn_rc_X = location[0];
            int[] del_location = new int[2];
            del_re.getLocationInWindow(del_location);
            int del_Y = del_location[1];
            int del_x = del_location[0];
            if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                if (!Environment.getExternalStorageDirectory().exists()) {
                    Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
                    return false;
                }
                System.out.println("2");
                if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {//判断手势按下的位置是否是语音录制按钮的范围内
                    System.out.println("3");
                    mBtnRcd.setBackgroundResource(R.mipmap.voice_rcd_btn_pressed);
                    rcChat_popup.setVisibility(View.VISIBLE);
                    voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    voice_rcd_hint_tooshort.setVisibility(View.GONE);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (!isShosrt) {
                                voice_rcd_hint_loading.setVisibility(View.GONE);
                                voice_rcd_hint_rcding
                                        .setVisibility(View.VISIBLE);
                            }
                        }
                    }, 300);
                    voice_img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    startVoiceT = System.currentTimeMillis();
                    //startVoiceT = SystemClock.currentThreadTimeMillis();
                    voiceName = startVoiceT + ".amr";
                    start(voiceName);
                    flag = 2;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {//松开手势时执行录制完成
                System.out.println("4");
                mBtnRcd.setBackgroundResource(R.mipmap.voice_rcd_btn_nor);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    rcChat_popup.setVisibility(View.GONE);
                    voice_img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    stop();
                    flag = 1;
                    File file = new File(android.os.Environment.getExternalStorageDirectory()+"/"
                            + voiceName);
                    if (file.exists()) {
                        file.delete();
                    }
                } else {

                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    stop();
                    endVoiceT = System.currentTimeMillis();
                    //endVoiceT = SystemClock.currentThreadTimeMillis();
                    flag = 1;
                    int time = (int) ((endVoiceT - startVoiceT) / 1000);
                    if (time < 1) {
                        isShosrt = true;
                        voice_rcd_hint_loading.setVisibility(View.GONE);
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                voice_rcd_hint_tooshort
                                        .setVisibility(View.GONE);
                                rcChat_popup.setVisibility(View.GONE);
                                isShosrt = false;
                            }
                        }, 500);
                        return false;
                    }
                    String voice_time = time+"\"";
                    sendVoice(voiceName,voice_time);
//                    Chat chat = new Chat();
//                    chat.setMessage_date(getDate());
//                    chat.setUser_name("高富帅");
//                    chat.setType(Chat.MSG_TYPE_SEND);
//                    chat.setUser_img("null");
//                    chat.setMessage_content(voiceName);
//                    chat.setTime(time+"\"");
//                    list.add(chat);
//                    adapter.notifyDataSetChanged();
//                    lv_chat.setSelection(lv_chat.getCount() - 1);
                    rcChat_popup.setVisibility(View.GONE);

                }
            }
            if (event.getY() < btn_rc_Y) {//手势按下的位置不在语音录制按钮的范围内
                System.out.println("5");
                Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc);
                Animation mBigAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc2);
                voice_img1.setVisibility(View.GONE);
                del_re.setVisibility(View.VISIBLE);
                del_re.setBackgroundResource(R.mipmap.voice_rcd_cancel_bg);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    del_re.setBackgroundResource(R.mipmap.voice_rcd_cancel_bg_focused);
                    voice_sc_img1.startAnimation(mLitteAnimation);
                    voice_sc_img1.startAnimation(mBigAnimation);
                }
            } else {

                voice_img1.setVisibility(View.VISIBLE);
                del_re.setVisibility(View.GONE);
                del_re.setBackgroundResource(0);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 录制完成时发送语音到服务器
     */
    private void sendVoice(String voiceName,String voice_time){
        Map<String,Object> map = new HashMap<>();
        map.put("File",SoundMeter.VOICE_FILE_DIR+"/"+voiceName);
        map.put("user_id",user_id+"");
        map.put("target_user_id",target_user_id+"");
        map.put("voice_time",voice_time);
        HttpServer.sendPostRequest(map,null,URLConstants.BASE_URL+URLConstants.SEND_VOICE_CHAT_URL,null);
        //发送完之后应该清除掉语音文件

    }
    /**
     * 得到当前日期
     * @return
     */
    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));

        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "/" + month + "/" + day + " " + hour + ":"
                + mins);

        return sbBuffer.toString();
    }
    //间隔
    private static final int POLL_INTERVAL = 300;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    /**
     * 录音时动画 根据振幅
     * @param signalEMA
     */
    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                voice_volume.setImageResource(R.mipmap.amp1);
                break;
            case 2:
            case 3:
                voice_volume.setImageResource(R.mipmap.amp2);

                break;
            case 4:
            case 5:
                voice_volume.setImageResource(R.mipmap.amp3);
                break;
            case 6:
            case 7:
                voice_volume.setImageResource(R.mipmap.amp4);
                break;
            case 8:
            case 9:
                voice_volume.setImageResource(R.mipmap.amp5);
                break;
            case 10:
            case 11:
                voice_volume.setImageResource(R.mipmap.amp6);
                break;
            default:
                voice_volume.setImageResource(R.mipmap.amp7);
                break;
        }
    }

    /**
     * 录音停止
     */
    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        voice_volume.setImageResource(R.mipmap.amp1);
    }
    /**
     * 开始录音
     * @param name  录音文件名
     */
    private void start(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mab);
    }
}
