package com.oracle.schoolcircle.content.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.entity.Content;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.DensityUtils;
import com.oracle.schoolcircle.utils.ImageUtils;
import com.oracle.schoolcircle.utils.ScreenUtils;
import com.oracle.schoolcircle.view.GridViewForScrollView;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class ContentAdapter extends BaseAdapter {
    private Context context;
    private List<Content> list;

    public ContentAdapter(Context context, List<Content> list) {
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
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_contents, null);
            //用户名  用户头像
            vh.tv_contents_user_name = (TextView) convertView.findViewById(R.id.tv_contents_user_name);
            vh.iv_contents_user_img = (ImageView) convertView.findViewById(R.id.iv_contents_user_img);
            //内容标题  内容日期  内容  内容图片
            vh.tv_contents_title = (TextView) convertView.findViewById(R.id.tv_contents_title);
            vh.tv_contents_date = (TextView) convertView.findViewById(R.id.tv_contents_date);
            vh.tv_contents_content = (TextView) convertView.findViewById(R.id.tv_contents_content);
//            vh.gv_content_pic = (NineGridImageView) convertView.findViewById(R.id.gv_content_pic);
            vh.gv_content_pic = (GridViewForScrollView) convertView.findViewById(R.id.gv_content_pic);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();

        }
        Content content = list.get(position);
        vh.tv_contents_user_name.setText(content.getUser_name());
        String user_img_url = content.getUser_img();
        if ("null".equals(user_img_url)) {
            Bitmap oldBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_th_icon_boy);
            Bitmap circleBitmap = ImageUtils.circleBitmap(oldBitmap);
            vh.iv_contents_user_img.setImageBitmap(circleBitmap);
        } else {
            //头像圆形切割
            ImageUtils.getBitmapUtils(context).display(vh.iv_contents_user_img, URLConstants.BASE_URL + user_img_url, new DefaultBitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                    Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                    container.setImageBitmap(circleBitmap);
                    container.invalidate();
                }
            });
        }
        vh.tv_contents_title.setText("#" + content.getContent_title() + "#");
        vh.tv_contents_content.setText(content.getContent_content());
        vh.tv_contents_date.setText(content.getContent_date());
        List<String> picList = content.getContent_pics();
//        ContentRVAdapter adapter = new ContentRVAdapter(context,picList);
//        vh.rv_content_pic.setLayoutManager(new GridLayoutManager(context,3));

        PicAdapter adapter = new PicAdapter(context,picList);
        vh.gv_content_pic.setAdapter(adapter);
        if (picList.size()==1){
            vh.gv_content_pic.setNumColumns(1);
        }
        if (picList.size()==2){
            vh.gv_content_pic.setNumColumns(2);
        }
        if (picList.size()>=3){
            vh.gv_content_pic.setNumColumns(3);
        }
//        NGIAdapter adapter = new NGIAdapter();
//        vh.gv_content_pic.setAdapter(adapter);
//        vh.gv_content_pic.setImagesData(picList);
        return convertView;
    }

    private class ViewHolder {
        //用户名  用户头像
        private TextView tv_contents_user_name;
        private ImageView iv_contents_user_img;
        //内容标题  内容日期  内容  内容图片
        private TextView tv_contents_title;
        private TextView tv_contents_content;
        private TextView tv_contents_date;
//        private NineGridImageView gv_content_pic;
        private GridViewForScrollView gv_content_pic;
    }
}
