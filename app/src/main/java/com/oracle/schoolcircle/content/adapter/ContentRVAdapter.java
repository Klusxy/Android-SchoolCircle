package com.oracle.schoolcircle.content.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oracle.schoolcircle.R;
import com.oracle.schoolcircle.http.constants.URLConstants;
import com.oracle.schoolcircle.utils.ImageUtils;

import java.util.List;

/**
 * Created by 田帅 on 2017/2/20.
 */

public class ContentRVAdapter extends RecyclerView.Adapter<ContentRVAdapter.ViewHolder>{
    private Context context;
    private List<String> list;

    public ContentRVAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(
                context).inflate(R.layout.rv_item_content, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUtils.getBitmapUtils(context).display(holder.iv_rv_contents_pic, URLConstants.BASE_URL + list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_rv_contents_pic;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_rv_contents_pic = (ImageView) itemView.findViewById(R.id.iv_rv_contents_pic);
        }
    }
}
