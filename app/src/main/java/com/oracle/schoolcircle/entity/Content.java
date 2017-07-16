package com.oracle.schoolcircle.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class Content implements Serializable{

    /**
     * content_id : 75
     * content_title : 测试标题
     * content_content : 测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容
     * user_id : 43
     * type_id : 23
     * user_name : 测试用户
     * type_name : 明星
     * content_pics : ["20170207/6eb52d91a55afa4b6ea86d2a5a2c909751c2.png","20170207/31c9dd08a26a7a4b9caa2d2a4f80fdf48592.png","20170207/01dbe7ffae98ba4d24ab814ad99021a32db3.png"]
     */
    private int content_id;
    private String content_title;
    private String content_content;
    private int user_id;
    private int type_id;
    private String user_name;
    private String type_name;
    private List<String> content_pics;
    private String content_date;
    private String user_img;

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getContent_date() {
        return content_date;
    }

    public void setContent_date(String content_date) {
        this.content_date = content_date;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getContent_title() {
        return content_title;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
    }

    public String getContent_content() {
        return content_content;
    }

    public void setContent_content(String content_content) {
        this.content_content = content_content;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public List<String> getContent_pics() {
        return content_pics;
    }

    public void setContent_pics(List<String> content_pics) {
        this.content_pics = content_pics;
    }
}
