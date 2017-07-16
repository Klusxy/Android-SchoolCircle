package com.oracle.schoolcircle.http.data;

import com.oracle.schoolcircle.entity.News;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class NewsData extends BeanData {
    private List<News> list;

    public List<News> getList() {
        return list;
    }

    public void setList(List<News> list) {
        this.list = list;
    }
}
