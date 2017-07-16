package com.oracle.schoolcircle.content.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.content.adapter.ContentSendGridViewAdapter;
import com.oracle.schoolcircle.entity.ContentType;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.CommonData;
import com.oracle.schoolcircle.http.parser.CommonParser;
import com.oracle.schoolcircle.my.activity.DetailMyActivity;
import com.oracle.schoolcircle.utils.CameraUtil;
import com.oracle.schoolcircle.utils.JudgeUtils;
import com.oracle.schoolcircle.utils.KeyBoardUtils;
import com.oracle.schoolcircle.view.BottomMenuDialog;
import com.oracle.schoolcircle.view.nicespinner.NiceSpinner;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/18.
 */

public class ContentSendActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final String TAG = "sdadasdas";
    private ImageView iv_content_send_close;
    private TextView tv_content_send;
    private List<ContentType> list;
    /**
     * 下拉框
     */
    private NiceSpinner ns_content_send_type;
    /**
     * 给spinner赋值的nameList
     */
    private List<String> nameList;
    /**
     * 内容类型
     */
    private int type_id = 0;
    /**
     * 点击屏幕隐藏软键盘
     */
    private InputMethodManager imm = null;
    private EditText et_content_send_title;
    private EditText et_content_send_content;

    /**
     * gridView 图片path集合
     */
    private List<String> picList;
    public final static String ADD_PIC = "com.oracle.addPic";
    private GridView gv_content_send_pics;
    private ContentSendGridViewAdapter picAdapter;
    /**
     * 底部弹出框
     */
    private BottomMenuDialog bottomMenuDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_content_send;
    }

    @Override
    public void initView() {
        iv_content_send_close = (ImageView) findViewById(R.id.iv_content_send_close);
        iv_content_send_close.setOnClickListener(this);
        tv_content_send = (TextView) findViewById(R.id.tv_content_send);
        tv_content_send.setOnClickListener(this);
        et_content_send_title = (EditText) findViewById(R.id.et_content_send_title);
        et_content_send_content = (EditText) findViewById(R.id.et_content_send_content);
        /**
         * 获取类型list,并对nameList赋值
         */
        setList();
        ns_content_send_type = (NiceSpinner) findViewById(R.id.ns_content_send_type);
        ns_content_send_type.setTextColor(getResources().getColor(R.color.main_color));
        ns_content_send_type.attachDataSource(nameList);
        ns_content_send_type.addOnItemClickListener(this);
        //设置默认type_id
        type_id = list.get(ns_content_send_type.getSelectedIndex()).getType_id();
        Log.d("sdadasdas", "内容类型id为：" + list.get(ns_content_send_type.getSelectedIndex()).getType_id());
        //点击空白地方关闭软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //对picList初始化
        initPicList();
        gv_content_send_pics = (GridView) findViewById(R.id.gv_content_send_pics);
        picAdapter = new ContentSendGridViewAdapter(this, picList);
        gv_content_send_pics.setAdapter(picAdapter);
        gv_content_send_pics.setOnItemClickListener(this);
    }

    /**
     * 对picList初始化
     */
    private void initPicList() {
        picList = new ArrayList<>();
        picList.add(ADD_PIC);
    }

    private void setList() {
        list = (List<ContentType>) getIntent().getExtras().getSerializable("list");
        nameList = new ArrayList<>();
        for (ContentType c : list) {
            String type_name = c.getType_name();
            nameList.add(type_name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 发布内容按钮
             */
            case R.id.tv_content_send:
                sendRequest();
                break;
            /**
             * 关闭按钮
             */
            case R.id.iv_content_send_close:
                finish();
                break;
        }
    }

    /**
     * 重写ontouch   实现点击屏幕隐藏软键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (ContentSendActivity.this.getCurrentFocus() != null) {
                if (ContentSendActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(ContentSendActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 发布内容请求
     */
    private void sendRequest() {
        //需要的参数有： 内容 content_content
        // 发布用户的id  user_id
        //  类型的id  type_id
        // 内容标题 content_title
        //  图片
        if (type_id == 0) {
            toast("请选择您要发布的内容类型");
        }else if (JudgeUtils.isEmpty(et_content_send_title.getText().toString())) {
            toast("内容标题不能为空");
        }else if (JudgeUtils.isEmpty(et_content_send_content.getText().toString())) {
            toast("内容不能为空");
        }
        else if (picList.size()==1){
            toast("图片不能为空");
        }else {
            //获取用户id
            SharedPreferences sp = getSharedPreferences("userinfo",MODE_PRIVATE);
            int user_id = sp.getInt("user_id",0);
            int type_id = list.get(ns_content_send_type.getSelectedIndex()).getType_id();
            String content_title = et_content_send_title.getText().toString();
            String content_content = et_content_send_content.getText().toString();
            for (int i = 0;i<picList.size();i++) {
                if (ADD_PIC.equals(picList.get(i))) {
                    picList.remove(i);
                    break;
                }
            }
            Log.d(TAG,"用户id为:"+user_id+"  标题为："+content_title+" 内容为:"+content_content
            +"  类型id为:"+type_id+"  几张照片:"+picList.size());
            //网络请求
            Map<String,Object> map = new HashMap<>();
            map.put("user_id",user_id+"");
            map.put("type_id",type_id+"");
            map.put("content_title",content_title);
            map.put("content_content",content_content);
            for (int i = 0;i<picList.size();i++) {
                String content_img = "File";
                map.put(content_img+i,picList.get(i));
            }
            CommonParser parser = new CommonParser();
            HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.ADD_CONTENT_URL, new Callback() {
                @Override
                public void success(BeanData beanData) {
                    CommonData commonData = (CommonData) beanData;;
                    if (StatusCode.Dao.INSERT_SUCCESS==commonData.getFlag()) {
                        toast("发布成功");
                        finish();
                    }
                }

                @Override
                public void failure(String error) {

                }
            });
        }

    }

    public static void actionStart(Context context, List<ContentType> list) {
        Intent intent = new Intent(context, ContentSendActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) list);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        /**
         * 下拉框
         */
        if (parent.getId() == R.id.ns_content_send_type) {
            type_id = list.get(position).getType_id();
            Log.d("sdadasdas", "内容类型id为：" + type_id);
        }
        /**
         * 照片网格
         */
        if (parent.getId() == R.id.gv_content_send_pics) {
            if (ADD_PIC.equals(picList.get(position))) {
                /**
                 * 参数分别代表
                 * 最多可选择图片数（int），
                 * 单选多选（int），model可以设置ImageSelectorActivity.MODE_MULTIPLE和mageSelectorActivity.MODE_SINGLE
                 *
                 * 是否显示拍照选项（boolean），
                 * 是否显示预览（boolean），
                 * 是否裁剪（boolean）等
                 *
                 */
                ImageSelectorActivity.start(this, 9, ImageSelectorActivity.MODE_MULTIPLE, true, true, true);
            } else {
                //不是添加图片
                bottomMenuDialog = new BottomMenuDialog.Builder(ContentSendActivity.this)
                        .setTitle("")
                        .addMenu("删除照片", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                picList.remove(position);
                                //判断集合中是否包含添加图片
                                if (isContainAddPic()) {

                                } else {
                                    picList.add(picList.size(), ADD_PIC);
                                }
                                picAdapter.notifyDataSetChanged();
                                bottomMenuDialog.dismiss();
                            }
                        }).create();

                bottomMenuDialog.show();
            }
        }
    }

    /**
     * 判断集合中是否含有添加图片
     * @return  true为含有
     */
    private boolean isContainAddPic() {
        for (String img_path : picList) {
            if (ADD_PIC.equals(img_path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {

            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
//            startActivity(new Intent(this,SelectResultActivity.class).putExtra(SelectResultActivity.EXTRA_IMAGES,images));
            Log.d("sdadasdas", "list长度   返回来的长度" + picList.size() + "  " + images.size());
            //判断是否已经超过九张
            if (picList.size() + images.size() > 10) {
                toast("最多只能选取九张图片");
            } else if (picList.size() + images.size() == 10) {
                picList.remove(ADD_PIC);
                for (String pic : images) {
                    picList.add(0, pic);
                }
            } else {
                for (String pic : images) {
                    picList.add(0, pic);
                }
            }
            picAdapter.notifyDataSetChanged();
        }
    }
}
