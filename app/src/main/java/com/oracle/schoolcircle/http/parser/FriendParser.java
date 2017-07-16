package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.FriendData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/15.
 */

public class FriendParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        FriendData friendData = new FriendData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            friendData.setCode(code);
            friendData.setFlag(flag);
            JSONArray arr = json.getJSONArray("list");
            List<User> list = new ArrayList<>();
            for (int i =0 ; i<arr.length();i++) {
                JSONObject userJson = arr.getJSONObject(i);
                int user_id = userJson.getInt("user_id");
                String user_tel = userJson.getString("user_tel");
                String user_name = userJson.getString("user_name");
                String user_img = userJson.getString("user_img");
                User u = new User();
                u.setUser_id(user_id);
                u.setUser_tel(user_tel);
                u.setUser_name(user_name);
                u.setUser_img(user_img);
                list.add(u);
            }
            friendData.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return friendData;
    }
}
