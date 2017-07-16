package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.Chat;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class ChatData extends BeanData {
    private List<Chat> list;

    public List<Chat> getList() {
        return list;
    }

    public void setList(List<Chat> list) {
        this.list = list;
    }
}
