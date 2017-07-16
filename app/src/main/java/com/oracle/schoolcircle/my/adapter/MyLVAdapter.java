package com.oracle.schoolcircle.my.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class MyLVAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> picIdList;
    private List<String> titleList;

    public MyLVAdapter(Context context, List<Integer> picIdList, List<String> titleList) {
        this.context = context;
        this.picIdList = picIdList;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return picIdList.size();
    }

    @Override
    public Object getItem(int position) {
        return picIdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0 || position == 2 || position == 4 || position == 9) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 2 || position == 4 || position == 9) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else {
            return 2;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder vh = null;
        if (convertView == null) {
            if (type == 0) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_my_other, null);
            } else if (type == 1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_one, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_my, null);
            }
            vh = new ViewHolder();
            vh.iv_my_ic = (ImageView) convertView.findViewById(R.id.iv_my_ic);
            vh.tv_my_title = (TextView) convertView.findViewById(R.id.tv_my_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (type == 0) {
        } else if (type == 1) {
            SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            String user_img = sp.getString("user_img", "");
            String user_name = sp.getString("user_name", "");
            if ("null".equals(user_img)) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_th_icon_boy);
                Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                vh.iv_my_ic.setImageBitmap(circleBitmap);
            } else {
                ImageUtils.getBitmapUtils(context).display(vh.iv_my_ic, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
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
            vh.tv_my_title.setText(user_name);
        } else {
            vh.iv_my_ic.setImageDrawable(context.getResources().getDrawable(picIdList.get(position)));
            vh.tv_my_title.setText(titleList.get(position));
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView iv_my_ic;
        public TextView tv_my_title;
    }
}

