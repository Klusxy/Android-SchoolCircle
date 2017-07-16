package com.oracle.schoolcircle.http.parser;

import android.util.Log;

import com.oracle.schoolcircle.entity.Content;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.ContentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class ContentParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        ContentData contentData =new ContentData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            contentData.setCode(code);
            contentData.setFlag(flag);
            JSONArray arr = json.getJSONArray("list");
            List<Content> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject contentJson = arr.getJSONObject(i);
                int content_id = contentJson.getInt("content_id");
                String content_title = contentJson.getString("content_title");
                String content_content = contentJson.getString("content_content");
                String content_date = contentJson.getString("content_date");
                int user_id = contentJson.getInt("user_id");
                int type_id = contentJson.getInt("type_id");
                String user_name = contentJson.getString("user_name");
                String type_name = contentJson.getString("type_name");
                String user_img = contentJson.getString("user_img");
                JSONArray picArr = contentJson.getJSONArray("content_pics");
                List<String> content_pics = new ArrayList<>();
                for (int m = 0; m < picArr.length(); m++) {
                    String content_pic = picArr.getString(m);
                    content_pics.add(content_pic);
                }
                Content c = new Content();
                c.setType_id(type_id);
                c.setType_name(type_name);
                c.setUser_id(user_id);
                c.setUser_name(user_name);
                c.setUser_img(user_img);
                c.setContent_id(content_id);
                c.setContent_title(content_title);
                c.setContent_content(content_content);
                c.setContent_pics(content_pics);
                c.setContent_date(content_date);
                list.add(c);
            }

            contentData.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentData;
    }
}
