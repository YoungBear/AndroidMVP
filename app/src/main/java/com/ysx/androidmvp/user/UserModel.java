package com.ysx.androidmvp.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.ysx.androidmvp.MyApplication;

/**
 * @author ysx
 * @date 2017/11/25
 * @description Model具体实现类，实现加载数据和保存数据的实际方法
 *
 * 调用UserModel加载数据时，需要先初始化MyApplication
 */

public class UserModel implements UserContract.Model {

    private static final String SP_KEY_NAME = "name";
    private static final String SP_KEY_AGE = "age";

    /**
     * @return
     * 从本地SharedPreferences中获取用户信息并返回
     */
    @Override
    public UserBean loadUser() {
        SharedPreferences sp = MyApplication.sContext.getSharedPreferences(
                MyApplication.SP_NAME, Context.MODE_PRIVATE);
        String name = sp.getString(SP_KEY_NAME, "defaultName");
        int age = sp.getInt(SP_KEY_AGE, 0);
        UserBean userBean = new UserBean();
        userBean.setName(name);
        userBean.setAge(age);
        return userBean;
    }

    @Override
    public boolean saveUser(String name, int age) {
        SharedPreferences sp = MyApplication.sContext.getSharedPreferences(
                MyApplication.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_KEY_NAME, name);
        editor.putInt(SP_KEY_AGE, age);
        return editor.commit();
    }
}
