package com.oracle.schoolcircle.content.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.DensityUtils;
import com.oracle.schoolcircle.utils.ImageUtils;
import com.oracle.schoolcircle.utils.ScreenUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/20.
 */

public class PicAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public PicAdapter(Context context, List<String> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.gv_item_pic,null);
            vh.iv_contents_pic = (ImageView) convertView.findViewById(R.id.iv_contents_pic);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        int screenWidth = ScreenUtils.getScreenWidth(context);
        if (list.size()==1){
            int picWidth = screenWidth - DensityUtils.dp2px(context,40);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(picWidth,picWidth);
            vh.iv_contents_pic.setLayoutParams(lp);
            vh.iv_contents_pic.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (list.size()==2){
            int picWidth = screenWidth/2 - DensityUtils.dp2px(context,40)/2;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(picWidth,picWidth);
            vh.iv_contents_pic.setLayoutParams(lp);
            vh.iv_contents_pic.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (list.size()>=3){
            int picWidth = screenWidth/3 - DensityUtils.dp2px(context,40)/3;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(picWidth,picWidth);
            vh.iv_contents_pic.setLayoutParams(lp);
            vh.iv_contents_pic.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        ImageUtils.getBitmapUtils(context).display(vh.iv_contents_pic, URLConstants.BASE_URL + list.get(position));

        return convertView;
    }
    private class ViewHolder{
        private ImageView iv_contents_pic;
    }
}
