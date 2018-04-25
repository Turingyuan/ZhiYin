package com.whut.zhiyin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.whut.zhiyin.JavaBean.User;
import com.whut.zhiyin.R;
import com.whut.zhiyin.model.FoodModelImpl;
import com.whut.zhiyin.model.UserModel;
import com.whut.zhiyin.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends AppCompatActivity {
    @BindView(R.id.register_back)
    ImageView registerBack;
    @BindView(R.id.register_name)
    EditText registerName;
    @BindView(R.id.register_phone)
    EditText registerPhone;
    @BindView(R.id.register_password)
    EditText registerPassword;
    @BindView(R.id.register_btn)
    Button registerBtn;

    private String mPhone;
    private UserModel mUserModel = new UserModel();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        //mPhone = getIntent().getStringExtra("phone");

    }

    @OnClick({R.id.register_back, R.id.register_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_btn:
                if (!TextUtils.isEmpty(registerName.getText().toString()) && !TextUtils.isEmpty(registerPassword.getText().toString())) {
                    User user = new User();
                    user.setName(registerName.getText().toString());
                    user.setPassword(registerPassword.getText().toString());
                    mPhone=registerPhone.getText().toString();
                    user.setNumber(mPhone);
                    mUserModel.onRegister(user, new FoodModelImpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void getFailure() {

                        }
                    });
                } else {
                    ToastUtils.showLong(RegistActivity.this, "请填写完整信息");
                }
                break;
        }
    }
}
