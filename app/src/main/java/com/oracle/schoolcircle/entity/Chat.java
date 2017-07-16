package com.oracle.schoolcircle.entity;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class Chat {
    /**
     * 发送方
     */
    public static final int MSG_TYPE_SEND = 0;
    /**
     * 接收方
     */
    public static final int MSG_TYPE_RECEIVED = 1;
    private int message_id;
    private int user_id;
    private int target_user_id;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String message_content;
    private String message_date;
    private String user_name;
    private int type;

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    private String user_img;
    public int getMessage_id() {
        return message_id;
    }
    public void setMessage_id(int message_id) {
        this.message_id = message_id;
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
    public String getMessage_date() {
        return message_date;
    }
    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }
}
