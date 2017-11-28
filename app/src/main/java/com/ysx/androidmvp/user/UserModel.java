package com.ysx.androidmvp.user;

import android.content.SharedPreferences;

/**
 * @author ysx
 * @date 2017/11/25
 * @description Model具体实现类，实现加载数据和保存数据的实际方法
 *
 * 调用UserModel加载数据时，需要先初始化MyApplication
 */

public class UserModel implements UserContract.Model {

    public static final String SP_KEY_NAME = "name";
    public static final String SP_KEY_AGE = "age";

    private final SharedPreferences mSharedPreferences;

    public UserModel(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

    /**
     * @return
     * 从本地SharedPreferences中获取用户信息并返回
     */
    @Override
    public UserBean loadUser() {
        String name = mSharedPreferences.getString(SP_KEY_NAME, "defaultName");
        int age = mSharedPreferences.getInt(SP_KEY_AGE, 0);
        UserBean userBean = new UserBean();
        userBean.setName(name);
        userBean.setAge(age);
        return userBean;
    }

    @Override
    public boolean saveUser(UserBean userBean) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SP_KEY_NAME, userBean.getName());
        editor.putInt(SP_KEY_AGE, userBean.getAge());
        return editor.commit();
    }
}
