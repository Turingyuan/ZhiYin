package com.whut.zhiyin.presenter;

import android.content.Context;
import android.content.Intent;

import com.whut.zhiyin.activity.LoginActivity;
import com.whut.zhiyin.model.UserModel;


/**
 * 作者：GXL on 2016/8/3 0003
 * 博客: http://blog.csdn.net/u014316462
 * 作用：用户页面Presenter
 */
public class UserFragmentPresenter {
    private UserModel mUserModel = new UserModel();

    public UserFragmentPresenter() {
    }

    /**
     * 登录
     *
     * @param context
     */
    public void onLogin(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    /**
     * 发说说
     *
     * @param context
     */
    /*
    public void onSendDynamic(Context context, User user) {
        if (mUserModel.isLogin()) {
            Intent intent = new Intent(context, SendDynamicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("User", user);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } else {
            onLogin(context);
        }
    }
    */

}
