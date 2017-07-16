package com.oracle.schoolcircle.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.CommonData;
import com.oracle.schoolcircle.http.data.LoginData;
import com.oracle.schoolcircle.http.parser.BeanParser;
import com.oracle.schoolcircle.http.parser.CommonParser;
import com.oracle.schoolcircle.http.parser.LoginParser;
import com.oracle.schoolcircle.main.MainActivity;
import com.oracle.schoolcircle.utils.JudgeUtils;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by 田帅 on 2017/2/8.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private final int REGISTER_SUCCESS = 10001;
    private Button btn_login_register;
    private EditText et_login_tel;
    private EditText et_login_password;
    private Button btn_login_login;
    //设定一个flag  判断是登陆还是注册
    private int loginOrRegisterFlag = 20001;
    InputMethodManager imm = null;
    //当注册成功后  利用该handler修改tel的Edittext
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_SUCCESS:
                    String tel = msg.obj.toString();
                    et_login_tel.setText(tel);
                    //密码框获取焦点
                    et_login_password.requestFocus();
                    //登陆按钮文字改为注册
                    btn_login_login.setText("注册");
                    loginOrRegisterFlag = 20002;
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        btn_login_register = (Button) findViewById(R.id.btn_login_register);
        btn_login_register.setOnClickListener(this);
        et_login_tel = (EditText) findViewById(R.id.et_login_tel);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login_login = (Button) findViewById(R.id.btn_login_login);
        btn_login_login.setOnClickListener(this);
        //点击空白地方关闭软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void login(Map<String, Object> map) {
        //登陆
        BeanParser parser = new LoginParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.LOGIN_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                LoginData loginData = (LoginData) beanData;
                if (loginData.getFlag() == StatusCode.Login.LOGIN_SUCCESS) {
                    toast("登陆成功");
                    User user = loginData.getUser();
                    //登陆成功之后将数据存入sp中
                    SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("user_id", user.getUser_id());
                    editor.putString("user_tel", user.getUser_tel());
                    editor.putString("user_pwd", user.getUser_pwd());
                    editor.putString("user_name", user.getUser_name());
                    editor.putString("user_img", user.getUser_img());
                    editor.apply();
                    //跳转到主界面
                    MainActivity.actionStart(LoginActivity.this);
                    finish();
                } else {
                    toast("用户名密码错误");
                }
            }

            @Override
            public void failure(String error) {
                toast("服务器无反应" + " " + error);
            }
        });
    }

    public void register(Map<String, Object> map) {
        BeanParser parser = new CommonParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.REGISTER_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                CommonData commonData = (CommonData) beanData;
                int user_id = commonData.getFlag();
                toast("注册成功");
                //登陆成功之后将数据存入sp中
                SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("user_id", user_id);
                editor.putString("user_tel", et_login_tel.getText().toString());
                editor.putString("user_pwd", et_login_password.getText().toString());
                editor.putString("user_name", et_login_tel.getText().toString());
                editor.putString("user_img", "null");
                editor.apply();
                MainActivity.actionStart(LoginActivity.this);
                finish();


            }

            @Override
            public void failure(String error) {
                toast("服务器无反应");
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (LoginActivity.this.getCurrentFocus() != null) {
                if (LoginActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登陆按钮
            case R.id.btn_login_login:
                if ("".equals(et_login_tel.getText().toString()) || "".equals(et_login_password.getText().toString())) {
                    toast("手机号和密码不能为空");
                } else {
                    String user_tel = et_login_tel.getText().toString();
                    String user_pwd = et_login_password.getText().toString();
                    //检验格式是否为手机号
                    //检验密码格式
                    if (JudgeUtils.isMobile(user_tel)) {
                        //将输入的手机号和密码存入map中
                        Map<String, Object> map = new HashMap<>();
                        map.put("user_tel", user_tel);
                        map.put("user_pwd", user_pwd);
                        if (loginOrRegisterFlag == 20001) {
                            login(map);
                        }
                        if (loginOrRegisterFlag == 20002) {
                            register(map);
                        }
                    } else {
                        toast("手机号格式不正确");
                    }


                }
                break;
            //注册按钮
            case R.id.btn_login_register:
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");

                            // 提交用户信息（此方法可以不调用）
                            registerUser(country, phone);
                        }
                    }
                });
                registerPage.show(this);
                break;
        }
    }

    private void registerUser(String country, String phone) {
        Message msg = handler.obtainMessage(REGISTER_SUCCESS, phone);
        handler.sendMessage(msg);
    }
}
