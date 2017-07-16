package com.oracle.schoolcircle.news.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseFragment;
import com.oracle.schoolcircle.entity.News;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.NewsData;
import com.oracle.schoolcircle.http.parser.NewsParser;
import com.oracle.schoolcircle.news.NewsDetailActivity;
import com.oracle.schoolcircle.news.adapter.NewsAdapter;
import com.oracle.schoolcircle.view.TopBarClickListener;
import com.oracle.schoolcircle.view.TopBarLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/8.
 */

public class NewsFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private ListView lv_main_news;
    private List<News> list;
    private NewsAdapter adapter = null;
    private TopBarLayout tbl_news;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (adapter == null) {
                adapter = new NewsAdapter(getActivity(),list);
                lv_main_news.setAdapter(adapter);
            }else {
                lv_main_news.notify();
            }
        }
    };
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        lv_main_news = (ListView) rootView.findViewById(R.id.lv_main_news);
        lv_main_news.setOnItemClickListener(this);
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        Map<String,Object> map = new HashMap<>();
        map.put("type","select");
        NewsParser parser = new NewsParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.NEWS_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                NewsData newsData = (NewsData) beanData;
                list = newsData.getList();
                new Thread(){
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                }.start();
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsDetailActivity.actionStart(getContext(),list.get(position));
    }
}
