package com.oracle.schoolcircle.scrip.adapter;

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
import com.oracle.schoolcircle.entity.MessageEntity;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class MessageLVAdapter extends BaseAdapter {
    private Context context;

    public MessageLVAdapter(Context context, List<MessageEntity> list) {
        this.context = context;
        this.list = list;
    }

    private List<MessageEntity> list;
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
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_message,null);
            vh.iv_scrip_message_user_img = (ImageView) convertView.findViewById(R.id.iv_scrip_message_user_img);
            vh.tv_scrip_message_user_name = (TextView) convertView.findViewById(R.id.tv_scrip_message_user_name);
            vh.tv_scrip_message_date = (TextView) convertView.findViewById(R.id.tv_scrip_message_date);
            vh.tv_scrip_message_content = (TextView) convertView.findViewById(R.id.tv_scrip_message_content);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        MessageEntity messageEntity = list.get(position);
        String user_img = messageEntity.getUser_img();
        if ("null".equals(user_img)) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_th_icon_boy);
            Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
            vh.iv_scrip_message_user_img.setImageBitmap(circleBitmap);
        }else {
            ImageUtils.getBitmapUtils(context).display(vh.iv_scrip_message_user_img, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                    imageView.setImageBitmap(circleBitmap);
                    //生效
                    imageView.invalidate();
                }

                @Override
                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

                }
            });
        }
        vh.tv_scrip_message_user_name.setText(messageEntity.getUser_name());
        vh.tv_scrip_message_date.setText(messageEntity.getMessage_date());

        /**
         * 对内容是否是语音消息进行判断
         */
        if (messageEntity.getMessage_content().contains(".amr")) {
            vh.tv_scrip_message_content.setText("[语音]");
        } else {
            vh.tv_scrip_message_content.setText(messageEntity.getMessage_content());
        }
        return convertView;
    }
    class ViewHolder{
        private ImageView iv_scrip_message_user_img;
        private TextView tv_scrip_message_user_name;
        private TextView tv_scrip_message_date;
        private TextView tv_scrip_message_content;
    }
}
