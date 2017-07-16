package com.oracle.schoolcircle.scrip.comparator;

import com.oracle.schoolcircle.entity.MessageEntity;

import java.util.Comparator;

/**
 * Created by 田帅 on 2017/2/15.
 */

public class MDateAscComparator implements Comparator<MessageEntity>{
    @Override
    public int compare(MessageEntity c1, MessageEntity c2) {
        return c1.getMessage_date().compareTo(c2.getMessage_date());
    }
}
