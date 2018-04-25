package com.whut.zhiyin.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.whut.zhiyin.JavaBean.User;
import com.whut.zhiyin.JavaBean.UserLocal;
import com.whut.zhiyin.R;
import com.whut.zhiyin.activity.LoginActivity;
import com.whut.zhiyin.activity.ShowMusicTeachVideoActivity;
import com.whut.zhiyin.application.BaseApplication;
import com.whut.zhiyin.eventbus.UserEventBus;
import com.whut.zhiyin.model.FoodModelImpl;
import com.whut.zhiyin.model.UserModel;
import com.whut.zhiyin.presenter.UserFragmentPresenter;
import com.whut.zhiyin.util.DialogBuilder;
import com.whut.zhiyin.util.ToastUtils;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;
import de.greenrobot.event.EventBus;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;


public class UserFragment extends Fragment {
    @BindView(R.id.UserPhoto)
    ImageView UserPhoto;
    @BindView(R.id.loginText)
    TextView loginText;
    @BindView(R.id.sendFood)
    TextView sendFood;
    @BindView(R.id.tv_showVideo)
    TextView sendDynamic;
    @BindView(R.id.love)
    TextView love;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.update)
    TextView update;
    @BindView(R.id.guanyu)
    TextView guanyu;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private final String LOGINUSER = "loginuser";
    private UserLocal mUserLocal;

    private UserFragmentPresenter mUserFragmentPresenter;
    private final int REQUEST_CODE = 0x01;

    private UserModel mUserModel = new UserModel();

    private Dialog mLoadingDialog;
    private Dialog mLoadingFinishDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserLocal = mUserModel.getUserLocal();
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);
        //Bmob.initialize(getContext(), "2a370dbb348f58f0faad200c12893aed");
        mUserFragmentPresenter = new UserFragmentPresenter();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_empty_dish)
                .showImageForEmptyUri(R.drawable.ic_empty_dish)
                .showImageOnFail(R.drawable.ic_empty_dish).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
        if (mUserLocal != null) {
            imageLoader.displayImage(mUserLocal.getPhoto(), UserPhoto, options);
            loginText.setText(mUserLocal.getName());
        }
        mLoadingDialog = DialogBuilder.createLoadingDialog(getActivity(), "正在上传图片");
        mLoadingFinishDialog = DialogBuilder.createLoadingfinishDialog(getActivity(), "上传完成");
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.UserPhoto, R.id.loginText, R.id.sendFood, R.id.tv_showVideo, R.id.love, R.id.send, R.id.update, R.id.guanyu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserPhoto:
                if (mUserModel.isLogin()) {
                    final PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                    intent.setPhotoCount(1);
                    intent.setShowCamera(true);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.loginText:
                mUserFragmentPresenter.onLogin(getActivity());
                break;
            case R.id.sendFood:
                ToastUtils.showLong(BaseApplication.getmContext(),"尚未开发，敬请期待");
                break;
            case R.id.tv_showVideo:
                /*
                if (mUserModel.isLogin()) {
                    mUserModel.getUser(mUserLocal.getObjectId(), new FoodModelImpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            mUserFragmentPresenter.onSendDynamic(getActivity(), (User) o);
                        }

                        @Override
                        public void getFailure() {

                        }
                    });
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }*/
                startActivity(new Intent(getActivity(), ShowMusicTeachVideoActivity.class));
                break;
            case R.id.love:

                //startActivity(new Intent(getActivity(), FoodLoveActivity.class));
                break;
            case R.id.send:
                //if (mUserModel.isLogin()) {
                    //startActivity(new Intent(getActivity(), MyDynamicActivity.class));
                //} else {
                    //startActivity(new Intent(getActivity(), LoginActivity.class));
                //}
                break;
            case R.id.update:
                BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                        if (updateStatus == UpdateStatus.No) {
                            Toast.makeText(getActivity(), "版本无更新", Toast.LENGTH_SHORT).show();
                        } else if (updateStatus == UpdateStatus.Yes) {
                            BmobUpdateAgent.forceUpdate(getActivity());
                        }
                    }
                });
                BmobUpdateAgent.update(getActivity());
                break;
            case R.id.guanyu:

                break;
        }
    }

    /**
     * Eventbus的处理函数
     *
     * @param event
     */
    public void onEventMainThread(UserEventBus event) {
        mUserLocal = event.getmUser();
        if (mUserLocal != null) {
            if (event.getmUser().getPhoto() != null) {
                imageLoader.displayImage(event.getmUser().getPhoto(), UserPhoto, options);
            }
            loginText.setText(event.getmUser().getName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if (requestCode == REQUEST_CODE && data != null) {
            mLoadingDialog.show();
            List<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            mUserModel.updateUserPhoto(pathList.get(0), mUserLocal.getObjectId(), new FoodModelImpl.BaseListener() {
                @Override
                public void getSuccess(Object o) {
                    mLoadingDialog.dismiss();
                    mLoadingFinishDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingFinishDialog.dismiss();
                        }
                    }, 500);
                    ToastUtils.showLong( BaseApplication.getmContext(),"头像修改成功");
                    User user = (User) o;
                    imageLoader.displayImage(user.getPhoto().getUrl(), UserPhoto, options);
                    UserLocal userLocal = new UserLocal();
                    userLocal.setName(user.getName());
                    userLocal.setObjectId(user.getObjectId());
                    userLocal.setNumber(user.getNumber());
                    if (user.getPhoto() != null) {
                        userLocal.setPhoto(user.getPhoto().getUrl());
                    }
                    mUserModel.putUserLocal(userLocal);
                }

                @Override
                public void getFailure() {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
