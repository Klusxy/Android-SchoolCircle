package com.oracle.schoolcircle.content.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.entity.Comment;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/20.
 */

public class CommentLVAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> list;

    public CommentLVAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh ;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_comment,null);
            vh.tv_comment_user_name = (TextView) convertView.findViewById(R.id.tv_comment_user_name);
            vh.tv_comment_date = (TextView) convertView.findViewById(R.id.tv_comment_date);
            vh.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
            vh.iv_comment_user_img = (ImageView) convertView.findViewById(R.id.iv_comment_user_img);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        Comment c = list.get(position);
        vh.tv_comment_user_name.setText(c.getUser_name());
        vh.tv_comment_date.setText(c.getComment_date());
        vh.tv_comment_content.setText(c.getComment_content());
        String user_img = c.getUser_img();
        if ("null".equals(user_img)) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_th_icon_boy);
            Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
            vh.iv_comment_user_img.setImageBitmap(circleBitmap);
        }else {
            ImageUtils.getBitmapUtils(context).display(vh.iv_comment_user_img, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                    imageView.setImageBitmap(circleBitmap);
                }

                @Override
                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

                }
            });
        }
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_comment_user_name;
        private TextView tv_comment_date;
        private TextView tv_comment_content;
        private ImageView iv_comment_user_img;
    }
}
