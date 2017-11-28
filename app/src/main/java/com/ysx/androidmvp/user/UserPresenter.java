package com.ysx.androidmvp.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.ysx.androidmvp.MyApplication;

/**
 * @author ysx
 * @date 2017/11/25
 * @description
 */

public class UserPresenter implements UserContract.Presenter {

    private final UserContract.Model mModel;
    private final UserContract.View mView;

    public UserPresenter(UserContract.View view) {
        mView = view;
        SharedPreferences sharedPreferences = MyApplication.sContext.getSharedPreferences(
                MyApplication.SP_NAME, Context.MODE_PRIVATE);
        mModel = new UserModel(sharedPreferences);
    }

    @Override
    public void loadUser() {
        UserBean userBean = mModel.loadUser();
        mView.setName(userBean.getName());
        mView.setAge(userBean.getAge());
    }

    @Override
    public boolean saveUser(String name, int age) {
        UserBean userBean = new UserBean();
        userBean.setName(name);
        userBean.setAge(age);
        return mModel.saveUser(userBean);
    }
}
