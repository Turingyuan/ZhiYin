package com.whut.zhiyin.JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 袁帅 on 2018/4/24.
 */

public class MusicTeachVideo extends BmobObject {
    String videoname;
    BmobFile video;
    public String getFoodname() {
        return videoname;
    }

    public void setFoodname(String videoname) {
        this.videoname=videoname;
    }

    public BmobFile getFoodvideo() {
        return video;
    }

    public void setFoodvideo(BmobFile video) {
        this.video = video;
    }



}
