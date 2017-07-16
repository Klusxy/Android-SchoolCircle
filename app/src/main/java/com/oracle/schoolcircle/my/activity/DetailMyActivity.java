package com.oracle.schoolcircle.my.activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.base.BaseActivity;
import com.oracle.schoolcircle.broadcast.BroConstants;
import com.oracle.schoolcircle.entity.User;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.StatusCode;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.CommonData;
import com.oracle.schoolcircle.http.data.UserData;
import com.oracle.schoolcircle.http.parser.CommonParser;
import com.oracle.schoolcircle.http.parser.UserParser;
import com.oracle.schoolcircle.my.fragment.MainPagerFragment;
import com.oracle.schoolcircle.utils.CameraUtil;
import com.oracle.schoolcircle.utils.ImageUtils;
import com.oracle.schoolcircle.view.BottomMenuDialog;
import com.oracle.schoolcircle.view.DampScrollView;
import com.oracle.schoolcircle.view.TopBarLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class DetailMyActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private ImageView iv_detail_my;
    private DampScrollView dsl_detail_my;
    private int user_id;
    private User user;
    private ImageView iv_user_img;
    private TextView tv_user_tel;
    private TextView tv_user_name;
    private TextView tv_user_update;
    private ImageView iv_user_back;
    private RelativeLayout rl_user_update;
    private BottomMenuDialog bottomMenuDialog;
    private UpdateUserBroadcast updateUserBroadcast = new UpdateUserBroadcast();

    /**
     * 更新头像的广播
     */
    private class UpdateUserBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String user_name = intent.getStringExtra("user_name");
            tv_user_name.setText(user_name);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_detail;
    }

    public static void actionStart(Context context, int user_id) {
        Intent intent = new Intent(context, DetailMyActivity.class);
        intent.putExtra("user_id", user_id);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        iv_detail_my = (ImageView) findViewById(R.id.iv_detail_my);
        dsl_detail_my = (DampScrollView) findViewById(R.id.dsl_detail_my);
        tv_user_tel = (TextView) findViewById(R.id.tv_user_tel);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        rl_user_update = (RelativeLayout) findViewById(R.id.rl_user_update);
        rl_user_update.setOnClickListener(this);
        iv_user_back = (ImageView) findViewById(R.id.iv_user_back);
        iv_user_back.setOnClickListener(this);
        iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
        iv_user_img.setOnClickListener(this);
        //设置阻尼效果图
        dsl_detail_my.setImageView(iv_detail_my);
        //获取用户id
        setUserId();
        //请求数据
        requestData();
        //设置viewpager
        setViewPager();
        //注册广播
        registerReceiver(updateUserBroadcast,new IntentFilter(BroConstants.UPDATE_USER));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateUserBroadcast);
    }

    private void setViewPager() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("主页", MainPagerFragment.class)
                .add("主题", MainPagerFragment.class)
                .add("相册", MainPagerFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_user);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.stl_user);
        viewPagerTab.setViewPager(viewPager);
    }

    private void requestData() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id + "");
        UserParser parser = new UserParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.USER_INFO_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                UserData userData = (UserData) beanData;
                if (StatusCode.Dao.SELECT_SUCCESS == userData.getFlag()) {
                    user = userData.getUser();
                    //请求数据成功，更新UI
                    updateUI();
                } else {
                    toast("请求数据失败");
                    finish();
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    private void updateUI() {
        if (user != null) {
            String user_img = user.getUser_img();
            String user_name = user.getUser_name();
            String user_tel = user.getUser_tel();
            if ("null".equals(user_img)) {
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_th_icon_boy);
                Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                iv_user_img.setImageBitmap(circleBitmap);
            } else {
                ImageUtils.getBitmapUtils(this).display(iv_user_img, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
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
            //name等
            tv_user_name.setText(user_name);
            tv_user_tel.setText(user_tel);
        }
    }


    private void setUserId() {
        user_id = getIntent().getIntExtra("user_id", 0);
        Log.d(TAG, "传过来的用户id为：" + user_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_back:
                finish();
                break;
            case R.id.rl_user_update:
                if (user != null) {
                    UserUpdateActivity.actionStart(this, user);
                } else {
                    toast("界面未初始化完成");
                }
                break;
            case R.id.iv_user_img:
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                bottomMenuDialog = new BottomMenuDialog.Builder(DetailMyActivity.this)
                        .setTitle("更换头像")
                        .addMenu("从手机相册选择", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 1);
                            }
                        }).addMenu("拍一张", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**
                                 * 拍照
                                 */
                                CameraUtil.takePhotoLarger(DetailMyActivity.this,2);
                            }
                        }).create();

                bottomMenuDialog.show();
                break;
        }
    }

    /**
     * 拍照之后或者选择完成之后发送图片到服务器
     * @param path
     */
    private void sendUserImg(final String path) {
        /**
         * 得到照片路径   发送请求
         */
        Map<String, Object> map = new HashMap<>();
        map.put("File", path);
        map.put("user_id",user_id+"");
        CommonParser parser = new CommonParser();
        HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.USER_UPDATE_IMG_URL, new Callback() {
            @Override
            public void success(BeanData beanData) {
                CommonData commonData = (CommonData) beanData;
                if (StatusCode.Dao.UPDATE_SUCCESS==commonData.getFlag()) {
                    toast("上传成功");
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                    iv_user_img.setImageBitmap(circleBitmap);
                    /**
                     * 发送广播更新顶部头像
                     */
                    Intent intent = new Intent(BroConstants.UPDATE_USER);
                    intent.putExtra("user_name",user.getUser_name());
                    sendBroadcast(intent);
                }
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    /**
     * 照相机、图库回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 系统图库
         */
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                //得到所有公开的内容提供者
                ContentResolver con = getContentResolver();
                //利用uri去查  得到cursor
                Cursor cur = con.query(uri, null, null, null, null);
                if (cur.moveToNext()) {
                    final String path = cur.getString(1);
                    Log.d(TAG, " 照片路径----------------->" + path);
                    bottomMenuDialog.dismiss();
                    sendUserImg(path);
                }
            }
        }
        /**
         * 拍照
         */
        if (requestCode==2) {
            String path = CameraUtil.getResultPhotoPathLarger();
            bottomMenuDialog.dismiss();
            sendUserImg(path);
        }
    }
}
