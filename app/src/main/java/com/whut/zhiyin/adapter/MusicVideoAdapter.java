package com.whut.zhiyin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.whut.zhiyin.JavaBean.MusicTeachVideo;
import com.whut.zhiyin.R;
import com.whut.zhiyin.application.BaseApplication;

import java.util.List;

/**
 * 作者：GXL on 2016/8/3 0003
 * 博客: http://blog.csdn.net/u014316462
 * 作用：展示可以播放的视频列表
 */
public class MusicVideoAdapter extends BaseAdapter {

    private List<MusicTeachVideo> mList;

    public MusicVideoAdapter(List<MusicTeachVideo> mList) {
        this.mList = mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(BaseApplication.getmContext()).inflate(R.layout.musicvideo_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.musicname);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        MusicTeachVideo item = (MusicTeachVideo) getItem(position);
        viewHolder.textView.setText(item.getFoodname());
        return view;
    }

    public static class ViewHolder {
        TextView textView;
    }
}
