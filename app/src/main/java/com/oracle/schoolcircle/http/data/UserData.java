package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.User;

/**
 * Created by 田帅 on 2017/2/17.
 */

public class UserData extends BeanData {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
