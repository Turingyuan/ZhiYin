package com.whut.zhiyin.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.whut.zhiyin.JavaBean.User;
import com.whut.zhiyin.JavaBean.UserLocal;
import com.whut.zhiyin.R;
import com.whut.zhiyin.application.BaseApplication;
import com.whut.zhiyin.eventbus.UserEventBus;
import com.whut.zhiyin.model.FoodModelImpl;
import com.whut.zhiyin.model.UserModel;
import com.whut.zhiyin.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    @BindView(R.id.login_back)
    ImageView loginBack;
    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.login_uname)
    EditText loginUname;
    @BindView(R.id.login_pass)
    EditText loginPass;

    private UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Bmob.initialize(this, "2a370dbb348f58f0faad200c12893aed");
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mUserModel=new UserModel();

    }
    @OnClick({R.id.login_back, R.id.login_register, R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_register:
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                finish();
                break;
            case R.id.login_btn:
                if (!TextUtils.isEmpty(loginUname.getText().toString()) && !TextUtils.isEmpty(loginPass.getText().toString())) {
                    mUserModel.getUser(loginUname.getText().toString(), loginPass.getText().toString(), new FoodModelImpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            ToastUtils.showLong(BaseApplication.getmContext(),"登录成功");
                            User user = (User) o;
                            UserLocal userLocal = new UserLocal();
                            userLocal.setName(user.getName());
                            userLocal.setObjectId(user.getObjectId());
                            userLocal.setNumber(user.getNumber());
                            if (user.getPhoto() != null) {
                                userLocal.setPhoto(user.getPhoto().getUrl());
                            }
                            mUserModel.putUserLocal(userLocal);
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            EventBus.getDefault().post(new UserEventBus(userLocal));
                            finish();

                        }

                        @Override
                        public void getFailure() {
                            ToastUtils.showLong(BaseApplication.getmContext(),"登录失败");
                        }
                    });

                }
                break;
        }
    }
}

