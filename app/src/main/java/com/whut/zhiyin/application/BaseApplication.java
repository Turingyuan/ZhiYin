package com.whut.zhiyin.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;

import cn.bmob.v3.Bmob;

/**
 * 作者：GXL on 2016/8/3 0003
 * 博客: http://blog.csdn.net/u014316462
 * 作用：全局Application
 */
public class BaseApplication extends MultiDexApplication {
    public static Context getmContext() {
        return sContext;
    }

    static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "409770c0c6eedf50d92e7af36cd78867");
        sContext = getApplicationContext();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
