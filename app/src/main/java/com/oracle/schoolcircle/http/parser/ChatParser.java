package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.Chat;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.ChatData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class ChatParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        ChatData chatData = new ChatData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            chatData.setCode(code);
            chatData.setFlag(flag);
            JSONArray arr = json.getJSONArray("list");
            List<Chat> list = new ArrayList<>();
            for (int i = 0;i<arr.length();i++) {
                JSONObject chatJson = arr.getJSONObject(i);
                int message_id = chatJson.getInt("message_id");
                int user_id = chatJson.getInt("user_id");
                int target_user_id = chatJson.getInt("target_user_id");
                String message_content = chatJson.getString("message_content");
                String message_date = chatJson.getString("message_date");
                String voice_time = chatJson.getString("voice_time");

                Chat chat = new Chat();
                chat.setMessage_id(message_id);
                chat.setUser_id(user_id);
                chat.setTarget_user_id(target_user_id);
                chat.setMessage_content(message_content);
                chat.setMessage_date(message_date);
                chat.setTime(voice_time);
                list.add(chat);
            }
                chatData.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chatData;
    }
}
