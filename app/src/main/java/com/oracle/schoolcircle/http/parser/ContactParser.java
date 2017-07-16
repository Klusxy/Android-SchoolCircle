package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.MessageEntity;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.MessageData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class ContactParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        MessageData messageData = new MessageData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            messageData.setCode(code);
            messageData.setFlag(flag);
            JSONArray arr = json.getJSONArray("list");
            List<MessageEntity> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject messageJson = arr.getJSONObject(i);
                int friend_id = messageJson.getInt("friend_id");
                String user_name = messageJson.getString("user_name");
                String user_img = messageJson.getString("user_img");
                String user_tel = messageJson.getString("user_tel");

                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setFriend_id(friend_id);
                messageEntity.setUser_name(user_name);
                messageEntity.setUser_img(user_img);
                messageEntity.setUser_tel(user_tel);
                list.add(messageEntity);
            }
            messageData.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageData;
    }
}
