package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.MessageEntity;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class MessageData extends BeanData {
    private List<MessageEntity> list;

    public List<MessageEntity> getList() {
        return list;
    }

    public void setList(List<MessageEntity> list) {
        this.list = list;
    }
}
