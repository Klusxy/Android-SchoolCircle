package com.oracle.schoolcircle.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.entity.News;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.ImageUtils;

/**
 * Created by 田帅 on 2017/2/19.
 */

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_news_detail_back;
    private News news;
    private TextView tv_news_detail_title;
    private TextView tv_news_detail_date;
    private TextView tv_news_detail_content;
    private ImageView iv_news_detail_img;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView() {
        iv_news_detail_back = (ImageView) findViewById(R.id.iv_news_detail_back);
        iv_news_detail_back.setOnClickListener(this);
        tv_news_detail_title = (TextView) findViewById(R.id.tv_news_detail_title);
        tv_news_detail_date = (TextView) findViewById(R.id.tv_news_detail_date);
        tv_news_detail_content = (TextView) findViewById(R.id.tv_news_detail_content);
        iv_news_detail_img = (ImageView) findViewById(R.id.iv_news_detail_img);
        //得到当前news
        setNews();
        //更新UI
        update();
    }


    private void update() {
        tv_news_detail_title.setText(news.getNews_title());
        tv_news_detail_date.setText(news.getNews_date());
        tv_news_detail_content.setText("\t\t\t\t"+news.getNews_content());
        ImageUtils.getBitmapUtils(this).display(iv_news_detail_img, URLConstants.BASE_URL + news.getNews_pic());
    }

    private void setNews() {
        news = (News) getIntent().getExtras().getSerializable("news");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_news_detail_back :
                finish();
                break;
        }
    }
    public static void actionStart(Context context , News news){
        Intent intent = new Intent(context,NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("news",news);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
