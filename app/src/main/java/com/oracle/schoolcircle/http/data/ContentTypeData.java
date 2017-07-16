package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.ContentType;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class ContentTypeData extends BeanData {
    private List<ContentType> list;

    public List<ContentType> getList() {
        return list;
    }

    public void setList(List<ContentType> list) {
        this.list = list;
    }
}
