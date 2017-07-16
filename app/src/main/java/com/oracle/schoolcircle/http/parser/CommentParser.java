package com.oracle.schoolcircle.http.parser;

import com.oracle.schoolcircle.entity.Chat;
import com.oracle.schoolcircle.entity.Comment;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.CommentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 田帅 on 2017/2/20.
 */

public class CommentParser extends BeanParser {
    @Override
    public BeanData parser(String result) {
        CommentData commentData = new CommentData();
        try {
            JSONObject json = new JSONObject(result);
            int code = json.getInt("code");
            int flag = json.getInt("flag");
            commentData.setCode(code);
            commentData.setFlag(flag);
            JSONArray arr = json.getJSONArray("list");
            if (arr!=null&&arr.length()!=0) {
                List<Comment> list = new ArrayList<>();
                for (int i = 0;i<arr.length();i++) {
                    JSONObject commentJson = arr.getJSONObject(i);
                    int user_id = commentJson.getInt("user_id");
                    String user_name = commentJson.getString("user_name");
                    String user_img = commentJson.getString("user_img");
                    String user_tel = commentJson.getString("user_tel");
                    int comment_id = commentJson.getInt("comment_id");
                    String comment_date = commentJson.getString("comment_date");
                    String comment_content = commentJson.getString("comment_content");
                    Comment comment= new Comment();
                    comment.setUser_id(user_id);
                    comment.setUser_name(user_name);
                    comment.setUser_img(user_img);
                    comment.setUser_tel(user_tel);
                    comment.setComment_id(comment_id);
                    comment.setComment_date(comment_date);
                    comment.setComment_content(comment_content);
                    list.add(comment);
                }
                commentData.setList(list);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentData;
    }
}
