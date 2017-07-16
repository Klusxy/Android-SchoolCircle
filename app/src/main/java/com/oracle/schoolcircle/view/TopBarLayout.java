package com.oracle.schoolcircle.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.broadcast.BroConstants;
import com.oracle.schoolcircle.http.Callback;
import com.oracle.schoolcircle.http.HttpServer;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.http.data.BeanData;
import com.oracle.schoolcircle.http.data.UserData;
import com.oracle.schoolcircle.http.parser.UserParser;
import com.oracle.schoolcircle.utils.DensityUtils;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 田帅 on 2017/2/12.
 */

public class TopBarLayout extends RelativeLayout implements TopBarClickListener{
    private Context context;
    /**
     * 左边圆形头像
     */
    private ImageView leftImageView;

    public TopBarClickListener getTopBarClickListener() {
        return topBarClickListener;
    }

    public void setTopBarClickListener(TopBarClickListener topBarClickListener) {
        this.topBarClickListener = topBarClickListener;
    }

    private TopBarClickListener topBarClickListener;
    /**
     * 中间标题
     */
    private TextView titleTextView;
    private String mTitleText;
    /**
     * 底部线条
     */
    private View bottomLine;

    /**
     * 用来更新头像的广播
     */
    private UpdateUserBroadcast updateUserBroadcast = new UpdateUserBroadcast();

    /**
     * 更新头像的广播
     */
    private class UpdateUserBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateImg();
        }
    }

    public TopBarLayout(Context context) {
        this(context, null);
    }

    public TopBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        //获取标题内容
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.TopBarLayout);
        mTitleText = arr.getString(R.styleable.TopBarLayout_titleText);
        titleTextView = new TextView(context);
        titleTextView.setText(mTitleText);
        titleTextView.setTextSize(23);
        titleTextView.setTextColor(getResources().getColor(R.color.main_color));
        titleTextView.setGravity(Gravity.CENTER);
        //注册广播
        context.registerReceiver(updateUserBroadcast,new IntentFilter(BroConstants.UPDATE_USER));
        LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(CENTER_VERTICAL, TRUE);
        titleParams.addRule(CENTER_HORIZONTAL, TRUE);
        addView(titleTextView, titleParams);
        //底部线条
        bottomLine = new View(context);
        bottomLine.setBackgroundColor(getResources().getColor(R.color.line_color));
        LayoutParams lineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, 1));
        lineParams.addRule(ALIGN_PARENT_BOTTOM);
        addView(bottomLine, lineParams);
        //左面圆形头像
        leftImageView = new ImageView(context);
        LayoutParams imageParams = new LayoutParams(DensityUtils.dp2px(context, 40), DensityUtils.dp2px(context, 40));
        imageParams.addRule(CENTER_VERTICAL, TRUE);
        imageParams.setMargins(DensityUtils.dp2px(context, 20), 0, 0, 0);
        //设置左边头像
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String user_img = sp.getString("user_img","");
        if (user_img == null) {
            //切割成圆形头像
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_th_icon_boy);
            Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
            leftImageView.setImageBitmap(circleBitmap);
        } else {
            //访问图片
            ImageUtils.getBitmapUtils(context).display(leftImageView, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
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
        addView(leftImageView, imageParams);
        leftImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topBarClickListener != null) {
                    topBarClickListener.onClickLeftImage();
                }
            }
        });
        setTopBarClickListener(this);
    }


    public void updateImg(){
        /**
         * 请求信息
         */
        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        int user_id = sp.getInt("user_id",0);
        if (user_id!=0) {
            /**
             * 发送请求
             */
            Map<String,Object> map = new HashMap<>();
            map.put("user_id",user_id+"");
            UserParser parser = new UserParser();
            HttpServer.sendPostRequest(map, parser, URLConstants.BASE_URL + URLConstants.USER_INFO_URL, new Callback() {
                @Override
                public void success(BeanData beanData) {
                    UserData userData = (UserData) beanData;
                    if (userData.getUser()!=null) {
                        String user_img = userData.getUser().getUser_img();
                        /**
                         * 更新sp中的数据
                         */
                        SharedPreferences sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("user_img",user_img);
                        editor.commit();
                        Intent intent = new Intent(BroConstants.UPDATE_USER_IMG);
                        intent.putExtra("user_img",user_img);
                        context.sendBroadcast(intent);
                        if (user_img == null) {
                            //切割成圆形头像
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_th_icon_boy);
                            Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
                            leftImageView.setImageBitmap(circleBitmap);
                        } else {
                            //访问图片
                            ImageUtils.getBitmapUtils(context).display(leftImageView, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
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
                    }
                }

                @Override
                public void failure(String error) {

                }
            });
        }else {
            //切割成圆形头像
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_th_icon_boy);
            Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
            leftImageView.setImageBitmap(circleBitmap);
        }

    }


    @Override
    public void onClickLeftImage() {
        Toast.makeText(context,"耀斌是猪呐",Toast.LENGTH_SHORT).show();
    }
}
