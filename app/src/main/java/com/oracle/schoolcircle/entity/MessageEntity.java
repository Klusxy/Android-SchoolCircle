package com.oracle.schoolcircle.entity;

import java.io.Serializable;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class MessageEntity implements Serializable{
    private int user_id;
    private int target_user_id;
    private int friend_id;
    private String user_tel;
    private String voice_time;

    public String getVoice_time() {
        return voice_time;
    }

    public void setVoice_time(String voice_time) {
        this.voice_time = voice_time;
    }

    public int getFriend_id() {
        return friend_id;
    }
    public String getUser_tel() {
        return user_tel;
    }
    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }
    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }
    private String user_name;
    private String user_img;
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_img() {
        return user_img;
    }
    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }
    private String message_content;
    private String message_date;
    public String getMessage_date() {
        return message_date;
    }
    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getTarget_user_id() {
        return target_user_id;
    }
    public void setTarget_user_id(int target_user_id) {
        this.target_user_id = target_user_id;
    }
    public String getMessage_content() {
        return message_content;
    }
    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }
}
