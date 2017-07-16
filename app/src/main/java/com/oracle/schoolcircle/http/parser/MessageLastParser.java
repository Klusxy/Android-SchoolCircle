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

public class MessageLastParser extends BeanParser {
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
                int user_id = messageJson.getInt("user_id");
                int target_user_id = messageJson.getInt("target_user_id");
                String user_name = messageJson.getString("user_name");
                String user_img = messageJson.getString("user_img");
                String message_content = messageJson.getString("message_content");
                String message_date = messageJson.getString("message_date");
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setUser_id(user_id);
                messageEntity.setTarget_user_id(target_user_id);
                messageEntity.setUser_name(user_name);
                messageEntity.setUser_img(user_img);
                messageEntity.setMessage_content(message_content);
                messageEntity.setMessage_date(message_date);
                list.add(messageEntity);
            }
            messageData.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageData;
    }
}
