package com.oracle.schoolcircle.content.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.content.activity.ContentSendActivity;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/18.
 */

public class ContentSendGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> picList;

    public ContentSendGridViewAdapter(Context context, List<String> picList) {
        this.context = context;
        this.picList = picList;
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int position) {
        return picList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView==null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gv_item_content_send_pic,null);
            vh.iv_content_send_pic = (ImageView) convertView.findViewById(R.id.iv_content_send_pic);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (ContentSendActivity.ADD_PIC.equals(picList.get(position))) {
            vh.iv_content_send_pic.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_playground_go_sub));
        }else {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            options.inSampleSize = 2;
//            options.inJustDecodeBounds = false;
//            options.inTempStorage = new byte[16 * 1024];
//            Bitmap bitmap = BitmapFactory.decodeFile(picList.get(position),options);
//            vh.iv_content_send_pic.setImageBitmap(bitmap);
            /**
             * 使用 xutils 防止OOM
             */
            ImageUtils.getBitmapUtils(context).display(vh.iv_content_send_pic,picList.get(position));
        }
        return convertView;
    }
    private class ViewHolder{
        private ImageView iv_content_send_pic;
    }
}
