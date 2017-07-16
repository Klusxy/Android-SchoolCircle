package com.oracle.schoolcircle.scrip.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.CommonData;
import com.oracle.schoolcircle.http.parser.CommonParser;
import com.oracle.schoolcircle.scrip.constants.ChatConstants;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/15.
 */

public class FriendAdapter extends BaseAdapter {
    private Context context;
    private List<User> list;

    public FriendAdapter(Context context, List<User> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_friend,null);
            vh.iv_friend_user_img = (ImageView) convertView.findViewById(R.id.iv_friend_user_img);
            vh.tv_friend_user_name = (TextView) convertView.findViewById(R.id.tv_friend_user_name);
            vh.tv_friend_user_tel = (TextView) convertView.findViewById(R.id.tv_friend_user_tel);
            vh.btn_friend_add = (Button) convertView.findViewById(R.id.btn_friend_add);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        User u = list.get(position);
        if ("null".equals(u.getUser_img())) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_th_icon_boy);
            Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
            vh.iv_friend_user_img.setImageBitmap(circleBitmap);
        }else {
            ImageUtils.getBitmapUtils(context).display(vh.iv_friend_user_img, URLConstants.BASE_URL + u.getUser_img(), new BitmapLoadCallBack<ImageView>() {
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
        vh.tv_friend_user_name.setText(u.getUser_name());
        //加下划线
        vh.tv_friend_user_tel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        vh.tv_friend_user_tel.setText(u.getUser_tel());
        /**
         * 设置点击事件
         */
        vh.btn_friend_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                final int user_id = sp.getInt("user_id",0);
                final int friend_id = list.get(position).getUser_id();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("type","add");
                map.put("user_id",user_id+"");
                map.put("friend_id",friend_id+"");
                CommonParser parser = new CommonParser();
                HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.ADD_FRIEND_URL, new Callback() {
                    @Override
                    public void success(BeanData beanData) {
                        CommonData commonData = (CommonData) beanData;
                        if (commonData.getFlag()== StatusCode.Dao.INSERT_SUCCESS) {
                            Log.d("mqtt","添加好友成功，准备发送广播通知主界面发送消息");
                            Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
                            /**
                             * 发送一个广播  通知发送消息
                             */
                            Intent intent = new Intent(ChatConstants.CHAT_FRIEND);
                            intent.putExtra("user_id",user_id);
                            intent.putExtra("friend_id",friend_id);
                            Log.d("mqtt","发送广播");
                            context.sendBroadcast(intent);
                            list.remove(position);
                            notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void failure(String error) {

                    }
                });

            }
        });
        return convertView;
    }
    class ViewHolder{
        private ImageView iv_friend_user_img;
        private TextView tv_friend_user_name;
        private TextView tv_friend_user_tel;
        private Button btn_friend_add;
    }
}
