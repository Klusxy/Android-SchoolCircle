package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.CommonData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 田帅 on 2017/2/8.
 */

public class CommonParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
            CommonData commonData = new CommonData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            commonData.setCode(code);
            commonData.setFlag(flag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commonData;
    }
}
