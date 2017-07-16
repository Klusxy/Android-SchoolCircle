package com.oracle.schoolcircle.content.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.content.adapter.CommentLVAdapter;
import com.oracle.schoolcircle.content.adapter.NGIAdapter;
import com.oracle.schoolcircle.content.adapter.PicAdapter;
import com.oracle.schoolcircle.content.comparator.CommentDateDescComparator;
import com.oracle.schoolcircle.entity.Comment;
import com.oracle.schoolcircle.entity.Content;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.CommentData;
import com.oracle.schoolcircle.http.data.CommonData;
import com.oracle.schoolcircle.http.parser.CommentParser;
import com.oracle.schoolcircle.http.parser.CommonParser;
import com.oracle.schoolcircle.utils.ImageUtils;
import com.oracle.schoolcircle.utils.JudgeUtils;
import com.oracle.schoolcircle.utils.KeyBoardUtils;
import com.oracle.schoolcircle.view.GridViewForScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/19.
 */

public class DetailContentActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "qrqweqwe";
    private ImageView iv_content_detail_back;
    private Content content;
    private ImageView iv_content_detail_user_img;
    private TextView tv_content_detail_user_name;
    private TextView tv_content_detail_content_date;
    private TextView tv_content_detail_content_title;
    private GridViewForScrollView ngi_content_detail;
//    private NGIAdapter ngiAdapter;
    private PicAdapter mAdapter;
    InputMethodManager imm = null;
    private Button btn_content_detail_comment_send;
    private EditText et_content_detail_comment_content;
    private ListView lv_content_detail_comment;
    private CommentLVAdapter adapter;
    private LinearLayout ll_content_commit_lv_flag;
    private TextView tv_content_detail_comment_count;
    private ScrollView sv_content_detail;
    /**
     * 用来标记有几条评论
     */
    private int commentCount = 0;
    /**
     * listview所需的数据源
     */
    private List<Comment> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_content_detail;
    }

    @Override
    public void initView() {
        iv_content_detail_back = (ImageView) findViewById(R.id.iv_content_detail_back);
        iv_content_detail_back.setOnClickListener(this);
        iv_content_detail_user_img = (ImageView) findViewById(R.id.iv_content_detail_user_img);
        tv_content_detail_user_name = (TextView) findViewById(R.id.tv_content_detail_user_name);
        tv_content_detail_content_date = (TextView) findViewById(R.id.tv_content_detail_content_date);
        tv_content_detail_content_title = (TextView) findViewById(R.id.tv_content_detail_content_title);
        ngi_content_detail = (GridViewForScrollView) findViewById(R.id.ngi_content_detail);
        btn_content_detail_comment_send = (Button) findViewById(R.id.btn_content_detail_comment_send);
        btn_content_detail_comment_send.setOnClickListener(this);
        et_content_detail_comment_content = (EditText) findViewById(R.id.et_content_detail_comment_content);
        lv_content_detail_comment = (ListView) findViewById(R.id.lv_content_detail_comment);
        ll_content_commit_lv_flag = (LinearLayout) findViewById(R.id.ll_content_commit_lv_flag);
        tv_content_detail_comment_count = (TextView) findViewById(R.id.tv_content_detail_comment_count);
        sv_content_detail = (ScrollView) findViewById(R.id.sv_content_detail);
//        ngiAdapter = new NGIAdapter();
        //从intent中取出content的值
        setContent();
        mAdapter = new PicAdapter(this,content.getContent_pics());
        ngi_content_detail.setAdapter(mAdapter);
        if (content.getContent_pics().size()==1){
            ngi_content_detail.setNumColumns(1);
        }
        if (content.getContent_pics().size()==2){
            ngi_content_detail.setNumColumns(2);
        }
        if (content.getContent_pics().size()>=3){
            ngi_content_detail.setNumColumns(3);
        }
//        ngi_content_detail.setImagesData(content.getContent_pics());

        //更新界面
        updateUI();
        //点击空白地方关闭软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //请求数据
        requestData();
        sv_content_detail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (DetailContentActivity.this.getCurrentFocus() != null) {
                    if (DetailContentActivity.this.getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(DetailContentActivity.this.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });
    }

    private void requestData() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "select");
        map.put("content_id", content.getContent_id() + "");
        CommentParser parser = new CommentParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.CONTENT_COMMENT_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                CommentData commentData = (CommentData) beanData;
                if (commentData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
                    //查询成功
                    if (commentData.getList() != null) {
                        lv_content_detail_comment.setVisibility(View.VISIBLE);
                        ll_content_commit_lv_flag.setVisibility(View.INVISIBLE);
                        commentCount = commentData.getList().size();
                        tv_content_detail_comment_count.setText("评论 "+commentCount+" 条");
                        list = commentData.getList();
                        Collections.sort(list,new CommentDateDescComparator());
                        adapter = new CommentLVAdapter(DetailContentActivity.this, list);
                        lv_content_detail_comment.setAdapter(adapter);
                    } else {
                        lv_content_detail_comment.setVisibility(View.INVISIBLE);
                        ll_content_commit_lv_flag.setVisibility(View.VISIBLE);
                    }

                } else {
                    lv_content_detail_comment.setVisibility(View.INVISIBLE);
                    ll_content_commit_lv_flag.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    private void updateUI() {
        if (content != null) {
            //给头像  姓名 日期赋值
            String user_img = content.getUser_img();
            if ("null".equals(user_img)) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_th_icon_boy);
                Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                iv_content_detail_user_img.setImageBitmap(circleBitmap);
            } else {
                ImageUtils.getBitmapUtils(this).display(iv_content_detail_user_img, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                        Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                        imageView.setImageBitmap(circleBitmap);
                        //生效
//                    imageView.invalidate();
                    }

                    @Override
                    public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

                    }
                });
            }
            String user_name = content.getUser_name();
            String content_date = content.getContent_date();
            tv_content_detail_user_name.setText(user_name);
            tv_content_detail_content_date.setText(content_date);
            String content_title = content.getContent_title();
            String content_content = content.getContent_content();
            tv_content_detail_content_title.setText("\t\t\t\t\t#" + content_title + "#\t\t" + content_content);
        }
    }

    private void setContent() {
        content = (Content) getIntent().getSerializableExtra("content");
        Log.d(TAG, content.getUser_name() + content.getContent_title());
    }

    public static void actionStart(Context context, Content c) {
        Intent intent = new Intent(context, DetailContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", c);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.iv_content_detail_back:
                finish();
                break;
            //发送评论
            case R.id.btn_content_detail_comment_send:
                if (JudgeUtils.isEmpty(et_content_detail_comment_content.getText().toString())) {
                    toast("评论内容不能为空");
                } else {
                    sendComment();
                }
                break;
        }
    }

    private void sendComment() {
        //当前用户id
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        String type = "add";
        final int user_id = sp.getInt("user_id", 0);
        final int content_id = content.getContent_id();
        final String comment_content = et_content_detail_comment_content.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("user_id", user_id + "");
        map.put("content_id", content_id + "");
        map.put("comment_content", comment_content);
        CommonParser parser = new CommonParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.CONTENT_COMMENT_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                CommonData commonData = (CommonData) beanData;
                if (commonData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
                    Comment c = new Comment();
                    c.setUser_id(user_id);
                    c.setContent_id(content_id);
                    c.setComment_content(comment_content);
                    toast("评论成功");
                    commentCount++;
                    updateListView(c);
                    tv_content_detail_comment_count.setText("评论 "+commentCount+" 条");
                    et_content_detail_comment_content.setText("");
                    KeyBoardUtils.closeKeybord(et_content_detail_comment_content,DetailContentActivity.this);
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    /**
     * 评论之后更新listview
     *
     * @param c
     */
    private void updateListView(Comment c) {
        //得到user_id  content_id  comment_content
        //还需要user_name  user_img user_tel comment_date
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        String user_name = sp.getString("user_name", "");
        String user_img = sp.getString("user_img", "");
        String user_tel = sp.getString("user_tel", "");
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dataDate = new Date(System.currentTimeMillis());
        String comment_date = dataFormat.format(dataDate);
        c.setUser_name(user_name);
        c.setUser_img(user_img);
        c.setUser_tel(user_tel);
        c.setComment_date(comment_date);
        /**
         * 判断当前listview是否存在
         */
        if (list == null) {
            list = new ArrayList<>();
            list.add(c);
            if (adapter == null) {
                adapter = new CommentLVAdapter(this, list);
                /**
                 * 显示lv  隐藏图片
                 */
                lv_content_detail_comment.setVisibility(View.VISIBLE);
                ll_content_commit_lv_flag.setVisibility(View.INVISIBLE);
                lv_content_detail_comment.setAdapter(adapter);
            }
        } else {
            list.add(c);
            ll_content_commit_lv_flag.setVisibility(View.INVISIBLE);
            Collections.sort(list, new CommentDateDescComparator());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("asdasdas","wwwww");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (DetailContentActivity.this.getCurrentFocus() != null) {
                if (DetailContentActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(DetailContentActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
