package com.oracle.schoolcircle.scrip.comparator;

import com.oracle.schoolcircle.entity.Chat;

import java.util.Comparator;


/**
 * Created by 田帅 on 2017/2/14.
 */

public class CDateAscComparator implements Comparator<Chat>{

    @Override
    public int compare(Chat c1, Chat c2) {
        return c1.getMessage_date().compareTo(c2.getMessage_date());
    }
}
