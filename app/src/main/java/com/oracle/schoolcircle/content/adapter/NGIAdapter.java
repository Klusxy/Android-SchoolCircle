package com.oracle.schoolcircle.content.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/19.
 */

public class NGIAdapter extends NineGridImageViewAdapter {
    @Override
    protected void onDisplayImage(Context context, ImageView imageView, Object o) {
        String content_pic = (String) o;

        ImageUtils.getBitmapUtils(context).display(imageView, URLConstants.BASE_URL + content_pic);
    }
    @Override
    protected ImageView generateImageView(Context context) {
        return super.generateImageView(context);
    }

}
