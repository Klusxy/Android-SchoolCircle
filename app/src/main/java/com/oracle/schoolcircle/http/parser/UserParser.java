package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.UserData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 田帅 on 2017/2/17.
 */

public class UserParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        UserData userData= new UserData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            userData.setCode(code);
            userData.setFlag(flag);
            JSONObject userJson = json.getJSONObject("user");
            int user_id = userJson.getInt("user_id");
            String user_name = userJson.getString("user_name");
            String user_img = userJson.getString("user_img");
            String user_tel = userJson.getString("user_tel");
            String user_pwd = userJson.getString("user_pwd");
            User user = new User();
            user.setUser_id(user_id);
            user.setUser_img(user_img);
            user.setUser_name(user_name);
            user.setUser_tel(user_tel);
            user.setUser_pwd(user_pwd);
            userData.setUser(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userData;
    }
}
