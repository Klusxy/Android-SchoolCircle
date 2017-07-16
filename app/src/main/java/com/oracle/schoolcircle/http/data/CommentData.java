package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.Comment;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/20.
 */

public class CommentData extends BeanData {
    private List<Comment> list;

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }
}
