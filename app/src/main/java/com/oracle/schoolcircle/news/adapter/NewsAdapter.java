package com.oracle.schoolcircle.news.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.entity.News;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.DensityUtils;
import com.oracle.schoolcircle.utils.ImageUtils;
import com.oracle.schoolcircle.utils.ScreenUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<News> list;

    public NewsAdapter(Context context, List<News> list) {
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
        if (convertView==null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_news,null);
            vh.tv_news_title = (TextView) convertView.findViewById(R.id.tv_news_title);
            vh.tv_news_date = (TextView) convertView.findViewById(R.id.tv_news_date);
            vh.iv_news_pic = (ImageView) convertView.findViewById(R.id.iv_news_pic);
            //设置大小
//            LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(  DensityUtils.dp2px(context,260), ViewGroup.LayoutParams.WRAP_CONTENT);
//            vh.tv_news_title.setLayoutParams(tvLp);
//            LinearLayout.LayoutParams ivLp = new LinearLayout.LayoutParams(DensityUtils.dp2px(context,120),DensityUtils.dp2px(context,80));
//            vh.iv_news_pic.setLayoutParams(ivLp);
            convertView.setTag(vh);
        }
            vh = (ViewHolder) convertView.getTag();
            String news_title = list.get(position).getNews_title();
            String news_date = list.get(position).getNews_date();
            String news_pic = list.get(position).getNews_pic();
            vh.tv_news_title.setText(news_title);
            vh.tv_news_date.setText(news_date);
            ImageUtils.getBitmapUtils(context).display(vh.iv_news_pic, URLConstants.BASE_URL + news_pic);

        return convertView;
    }
    private class ViewHolder {
        private TextView tv_news_title;
        private TextView tv_news_date;
        private ImageView iv_news_pic;
    }
}
