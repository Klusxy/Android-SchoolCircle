package com.oracle.schoolcircle.content.comparator;

import com.oracle.schoolcircle.entity.Comment;
import com.oracle.schoolcircle.entity.MessageEntity;

import java.util.Comparator;

/**
 * Created by 田帅 on 2017/2/15.
 * 日期的降序
 */

public class CommentDateDescComparator implements Comparator<Comment>{
    @Override
    public int compare(Comment c1, Comment c2) {
        return c2.getComment_date().compareTo(c1.getComment_date());
    }
}
