package com.oracle.schoolcircle.entity;

import java.io.Serializable;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class ContentType implements Serializable{
    private int type_id;
    private String type_name;

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
