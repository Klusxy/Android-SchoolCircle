package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.News;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.NewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class NewsParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        NewsData newsData = new NewsData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            newsData.setCode(code);
            newsData.setFlag(flag);
            JSONArray arr = json.getJSONArray("list");
            List<News> list = new ArrayList<>();
            for (int i = 0 ; i < arr.length() ; i++) {
                JSONObject newsJson = arr.getJSONObject(i);
                int news_id = newsJson.getInt("news_id");
                String news_title = newsJson.getString("news_title");
                String news_content = newsJson.getString("news_content");
                String news_pic = newsJson.getString("news_pic");
                String news_date = newsJson.getString("news_date");
                News news = new News();
                news.setNews_id(news_id);
                news.setNews_title(news_title);
                news.setNews_content(news_content);
                news.setNews_date(news_date);
                news.setNews_pic(news_pic);
                list.add(news);
            }
            newsData.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsData;
    }
}
