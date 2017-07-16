package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.ContentType;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.ContentTypeData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class ContentTypeParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        ContentTypeData contentTypeData = new ContentTypeData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            contentTypeData.setCode(code);
            contentTypeData.setFlag(flag);
            JSONArray arr = json.getJSONArray("list");
            List<ContentType> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject typeJson = arr.getJSONObject(i);
                int type_id = typeJson.getInt("type_id");
                String type_name = typeJson.getString("type_name");
                ContentType contentType = new ContentType();
                contentType.setType_id(type_id);
                contentType.setType_name(type_name);
                list.add(contentType);
            }
            contentTypeData.setList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentTypeData;
    }
}
