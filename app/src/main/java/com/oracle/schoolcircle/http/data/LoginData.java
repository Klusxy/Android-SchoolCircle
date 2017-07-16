package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.User;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class LoginData extends BeanData {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
