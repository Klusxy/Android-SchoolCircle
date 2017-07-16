package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.LoginData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class LoginParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        LoginData loginData = new LoginData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            loginData.setCode(code);
            loginData.setFlag(flag);
            JSONObject userJson = json.getJSONObject("user");
            int user_id = userJson.getInt("user_id");
            String user_tel = userJson.getString("user_tel");
            String user_pwd = userJson.getString("user_pwd");
            String user_name = userJson.getString("user_name");
            String user_img = userJson.getString("user_img");
            User user= new User();
            user.setUser_id(user_id);
            user.setUser_tel(user_tel);
            user.setUser_pwd(user_pwd);
            user.setUser_name(user_name);
            user.setUser_img(user_img);
            loginData.setUser(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginData;
    }
}
