package com.oracle.schoolcircle.my.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.broadcast.BroConstants;
import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.CommonData;
import com.oracle.schoolcircle.http.parser.CommonParser;
import com.oracle.schoolcircle.login.LoginActivity;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/17.
 */

public class UserUpdateActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private ImageView iv_update_back;
    private TextView tv_update_save;
    private TextView tv_update_user_tel;
    private EditText et_update_user_name;
    private User user;
    InputMethodManager imm = null;
    @Override
    public int getLayoutId() {
        return R.layout.activty_userupdate;
    }

    @Override
    public void initView() {
        iv_update_back = (ImageView) findViewById(R.id.iv_update_back);
        iv_update_back.setOnClickListener(this);
        tv_update_save = (TextView) findViewById(R.id.tv_update_save);
        tv_update_save.setOnClickListener(this);
        tv_update_user_tel = (TextView) findViewById(R.id.tv_update_user_tel);
        et_update_user_name = (EditText) findViewById(R.id.et_update_user_name);
        //从bundle中取出user 并赋值
        setUser();
        //更新UI
        updateUI();
        //点击空白地方关闭软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void updateUI() {
        tv_update_user_tel.setText(user.getUser_tel());
        et_update_user_name.setText(user.getUser_name());
    }

    private void setUser() {
        user = (User) getIntent().getSerializableExtra("user");
        Log.d(TAG, "传过来的用户名为：" + user.getUser_name());
    }

    public static void actionStart(Context context, User user) {
        Intent intent = new Intent(context,UserUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_update_back:
                finish();
                break;
            case R.id.tv_update_save:
                //发送请求
                sendUserInfo();
                break;
        }
    }

    /**
     * 发送请求
     */
    private void sendUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",user.getUser_id()+"");
        map.put("user_name",et_update_user_name.getText().toString());
        CommonParser parser = new CommonParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.USER_UPDATE_INFO_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                CommonData commonData = (CommonData) beanData;
                if (StatusCode.Dao.UPDATE_SUCCESS==commonData.getFlag()) {
                    toast("更新成功");
                    /**
                     * 更新sp中的数据
                     */
                    SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("user_name",et_update_user_name.getText().toString());
                    editor.commit();
                    /**
                     * 发送广播
                     */
                    Intent intent = new Intent(BroConstants.UPDATE_USER);
                    intent.putExtra("user_name",et_update_user_name.getText().toString());
                    sendBroadcast(intent);
                    finish();
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    /**
     * 重写ontouch   实现点击屏幕隐藏软键盘
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (UserUpdateActivity.this.getCurrentFocus() != null) {
                if (UserUpdateActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(UserUpdateActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
