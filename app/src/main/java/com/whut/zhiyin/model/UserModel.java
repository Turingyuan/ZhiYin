package com.whut.zhiyin.model;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.whut.zhiyin.JavaBean.User;
import com.whut.zhiyin.JavaBean.UserLocal;
import com.whut.zhiyin.application.BaseApplication;
import com.whut.zhiyin.util.SPUtils;
import com.whut.zhiyin.util.ToastUtils;


import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * 作者：Turingyuan on 2018/4/22
 * 作用：用户信息Model
 */
public class UserModel implements UserModelImpl {

    private final String LOGINUSER = "loginuser";

    /**
     * 用户登录验证
     *
     * @param phone
     * @param passoword
     * @param listener
     */
    @Override
    public void getUser(String phone, String passoword, final FoodModelImpl.BaseListener listener) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("Number", phone);
        query.addWhereEqualTo("Password", passoword);
        //query.setLimit(1);
        query.findObjects(BaseApplication.getmContext(), new FindListener<User>() {
            //        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> object, BmobException e) {
//                if (e==null) {
//                    if (object != null && object.size() != 0) {
//                        SPUtils.put(BaseApplication.getmContext(), LOGINUSER, object.get(0));
//                        listener.getSuccess(object.get(0));
//                    }
//                    else {
//                        listener.getFailure();
//                    }
//                }
//            }
//        });
//        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> list, BmobException e) {
//                if(e==null){
//                    if(list!=null&&list.size()!=0){
//                       SPUtils.put(BaseApplication.getmContext(), LOGINUSER, list.get(0));
//                       listener.getSuccess(list.get(0));
//                    }
//                }
//                else{
//                    listener.getFailure();
//                }
//            }
//        });
            @Override
            public void onSuccess(List<User> object) {
                if (object != null && object.size() != 0) {
                    SPUtils.put(BaseApplication.getmContext(), LOGINUSER, object.get(0));
                    listener.getSuccess(object.get(0));
                } else {
                    listener.getFailure();
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        });


    }

    /**
     * 根据objectId获取User
     *
     * @param objectId
     * @param listener
     */
    @Override
    public void getUser(String objectId, final FoodModelImpl.BaseListener listener) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", objectId);
        query.setLimit(1);

        query.findObjects(BaseApplication.getmContext(),new FindListener<User>() {
            @Override
            public void onSuccess(List<User> object) {
                if (object != null && object.size() != 0) {
                    SPUtils.put(BaseApplication.getmContext(), LOGINUSER, object.get(0));
                    listener.getSuccess(object.get(0));
                } else {
                    listener.getFailure();
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        });

        //bmob版本修改
//        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> list, BmobException e) {
//                if (e==null){
//                    if (list != null && list.size() != 0) {
//                        SPUtils.put(BaseApplication.getmContext(), LOGINUSER, list.get(0));
//                        listener.getSuccess(list.get(0));
//                    } else {
//                        listener.getFailure();
//                    }
//                }
//
//            }
//        });
    }

    /**
     * 更换用户的头像
     *
     * @param path
     * @param listener
     */
    public void updateUserPhoto(String path, final String objectId, final FoodModelImpl.BaseListener listener) {
        final BmobFile bmobFile = new BmobFile(new File(path));
//        bmobFile.upload(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    final User user = new User();
//                    user.setPhoto(bmobFile);
//                    user.update(objectId, new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if(e==null){
//                                listener.getSuccess(user);
//                            }
//                            else{
//                                listener.getFailure();
//                            }
//                        }
//                    });
//                }
//                else{
//                    listener.getFailure();
//                }
//            }
//        });
        //此方法已被更新
        bmobFile.upload(BaseApplication.getmContext(), new UploadFileListener() {
            @Override
            public void onSuccess() {
                final User user = new User();
                user.setPhoto(bmobFile);
                user.update(BaseApplication.getmContext(), objectId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        listener.getSuccess(user);

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        listener.getFailure();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.getFailure();
            }
        });

    }

    /**
     * 判断当前用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        List<UserLocal> list = new Select().from(UserLocal.class).execute();
        if (list.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将当前登录的对象保持到数据库中
     *
     * @param userLocal
     */
    public void putUserLocal(UserLocal userLocal) {
        new Delete().from(UserLocal.class).execute();
        userLocal.save();
    }

    /**
     * 获取当前登录的对象
     *
     * @return
     */
    public UserLocal getUserLocal() {
        return new Select().from(UserLocal.class).executeSingle();
    }


    /**
     * 注册功能
     *
     * @param user
     */
    public void onRegister(User user, final FoodModelImpl.BaseListener listener) {
//        user.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e==null){
//                    ToastUtils.show("注册成功");
//                    listener.getSuccess(null);
//                }
//                else {
//                    ToastUtils.show( "注册失败");
//                    listener.getFailure();
//                }
//            }
//        });
        //此方法已被更新
        user.save(BaseApplication.getmContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtils.showLong(BaseApplication.getmContext(),"注册成功");
                listener.getSuccess(null);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showLong(BaseApplication.getmContext(),"注册失败");
                listener.getFailure();
            }
        });

    }

    /**
     * 判断当前手机号码是否注册
     *
     * @param phone
     * @param listener
     * @return
     */
    public void isPhoneRegister(String phone, final FoodModelImpl.BaseListener listener) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("Number", phone);
        query.setLimit(1);
//        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> object, BmobException e) {
//                if(e==null){
//                    if (object != null && object.size() != 0) {
//                        listener.getSuccess(object.get(0));
//                    } else {
//                        listener.getFailure();
//                    }
//                }
//            }
//        });
        //此方法已被更新
        query.findObjects(BaseApplication.getmContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> object) {
                if (object != null && object.size() != 0) {
                    listener.getSuccess(object.get(0));
                } else {
                    listener.getFailure();
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        });

    }


}
