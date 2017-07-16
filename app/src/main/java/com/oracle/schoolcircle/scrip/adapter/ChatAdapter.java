package com.oracle.schoolcircle.scrip.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
import com.oracle.schoolcircle.entity.Chat;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.scrip.voice.SoundMeter;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/13.
 */

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<Chat> list;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    public ChatAdapter(Context context, List<Chat> list) {
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
    public int getItemViewType(int position) {
        Chat c = list.get(position);
        if (Chat.MSG_TYPE_SEND==c.getType()){
            return 0;
        }
        if (Chat.MSG_TYPE_RECEIVED==c.getType()) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Chat chat = list.get(position);
        ViewHolder vh = null;
        if (convertView==null) {
            if (Chat.MSG_TYPE_SEND==chat.getType()) {
                //发送方 右
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_right,null);
            }else {
                convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_left,null);
            }
            vh = new ViewHolder();
            vh.iv_chat_user_img = (ImageView) convertView.findViewById(R.id.iv_chat_user_img);
            vh.tv_chat_content = (TextView) convertView.findViewById(R.id.tv_chat_content);
            vh.tv_chat_user_name = (TextView) convertView.findViewById(R.id.tv_chat_user_name);
            vh.tv_chat_date = (TextView) convertView.findViewById(R.id.tv_chat_date);
            vh.tv_chat_time = (TextView) convertView.findViewById(R.id.tv_chat_time);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        String user_img = chat.getUser_img();
        if ("null".equals(user_img)) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_th_icon_boy);
            Bitmap circleBitmap = ImageUtils.circleBitmap(bitmap);
            vh.iv_chat_user_img.setImageBitmap(circleBitmap);
        }else {
            ImageUtils.getBitmapUtils(context).display(vh.iv_chat_user_img, URLConstants.BASE_URL + user_img, new BitmapLoadCallBack<ImageView>() {
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
        /**
         * 对内容是否是语音消息进行判断
         */
        if (chat.getMessage_content().contains(".amr")) {
            vh.tv_chat_content.setText("");
            vh.tv_chat_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.chatto_voice_playing, 0);
            vh.tv_chat_time.setText(chat.getTime());
        } else {
            vh.tv_chat_content.setText(chat.getMessage_content());
            vh.tv_chat_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            vh.tv_chat_time.setText("");
        }
        /**
         * 如果是语音 对内容设置点击事件
         */
        vh.tv_chat_content.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (chat.getMessage_content().contains(".amr")) {
                    playMusic(chat.getMessage_content()) ;
                }
            }
        });

        vh.tv_chat_user_name.setText(chat.getUser_name());
        vh.tv_chat_date.setText(chat.getMessage_date());
        return convertView;
    }
    class ViewHolder{
        private ImageView iv_chat_user_img;
        private TextView tv_chat_content;
        private TextView tv_chat_user_name;
        private TextView tv_chat_date;
        private TextView tv_chat_time;
    }
    /**
     * @Description  播放语音
     * @param name
     */
    private void playMusic(String name) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(URLConstants.BASE_URL+name);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
