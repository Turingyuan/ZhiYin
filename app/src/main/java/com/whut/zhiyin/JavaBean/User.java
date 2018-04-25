package com.whut.zhiyin.JavaBean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作用：用户表
 */
public class User extends BmobObject implements Serializable {

    //姓名
    String Name;
    //头像
    BmobFile Photo;
    //密码
    String Password;

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    //手机号码
    String Number;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public BmobFile getPhoto() {
        return Photo;
    }

    public void setPhoto(BmobFile photo) {
        Photo = photo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public User() {
    }
}
