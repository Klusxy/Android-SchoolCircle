package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.User;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/15.
 */

public class FriendData extends BeanData {
    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
