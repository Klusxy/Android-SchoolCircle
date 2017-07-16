package com.oracle.schoolcircle.content.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseFragment;
import com.oracle.schoolcircle.content.activity.DetailContentActivity;
import com.oracle.schoolcircle.content.adapter.ContentAdapter;
import com.oracle.schoolcircle.entity.Content;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.ContentData;
import com.oracle.schoolcircle.http.parser.ContentParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/9.
 */

public class ContentFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private ListView lv_school_content;
    private ContentAdapter adapter;
    private List<Content> allList;
    int type_id = 0;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initView() {
        lv_school_content = (ListView) rootView.findViewById(R.id.lv_school_content);
        int position = FragmentPagerItem.getPosition(getArguments());
        type_id = SchoolFragment.list.get(position).getType_id();
        lv_school_content.setOnItemClickListener(this);
        requestContentData(type_id);
    }

    public void getType_id(int type_id) {
        this.type_id = type_id;
    }

    public void requestContentData(int type_id) {
        //请求数据
        Map<String, Object> map = new HashMap<>();
        map.put("type_id", type_id + "");
        ContentParser parser = new ContentParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.CONTENT_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                ContentData contentData = (ContentData) beanData;
                if (contentData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
                    allList = contentData.getList();
                    adapter = new ContentAdapter(getContext(), allList);
                    lv_school_content.setAdapter(adapter);
                } else {
//                    toast("请求数据失败");
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailContentActivity.actionStart(getContext(),allList.get(position));
    }
}
