package com.oracle.schoolcircle.entity;

/**
 * Created by 田帅 on 2017/2/20.
 */

public class Comment {
    private int comment_id;
    private int user_id;
    private int content_id;
    private String comment_date;
    private String comment_content;
    private String user_name;
    private String user_img;
    private String user_tel;

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
    public String getUser_tel() {
        return user_tel;
    }
    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }
    public int getComment_id() {
        return comment_id;
    }
    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getContent_id() {
        return content_id;
    }
    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }
    public String getComment_date() {
        return comment_date;
    }
    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }
    public String getComment_content() {
        return comment_content;
    }
    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }
}
