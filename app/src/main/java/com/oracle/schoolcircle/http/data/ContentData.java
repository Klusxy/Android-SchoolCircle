package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.Content;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class ContentData extends BeanData {
    private List<Content> list;

    public List<Content> getList() {
        return list;
    }

    public void setList(List<Content> list) {
        this.list = list;
    }
}
