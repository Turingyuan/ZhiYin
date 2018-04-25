package com.whut.zhiyin.model;

import com.whut.zhiyin.JavaBean.MusicTeachVideo;
import com.whut.zhiyin.application.BaseApplication;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 袁帅 on 2018/4/24.
 */

public class MusicVideoModel implements MusicVideoModelImpl {

    /**
     * 获取视频列表
     * @param listener
     */
    @Override
    public void getGeneralFoodsVideoItem(final FoodModelImpl.BaseListener listener) {
        BmobQuery<MusicTeachVideo> query=new BmobQuery<MusicTeachVideo>();
        query.findObjects(BaseApplication.getmContext(), new FindListener<MusicTeachVideo>() {
            @Override
            public void onSuccess(List<MusicTeachVideo> list) {
                listener.getSuccess(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }
}
