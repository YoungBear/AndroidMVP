package com.ysx.androidmvp.user;

/**
 * @author ysx
 * @date 2017/11/25
 * @description
 */

public class UserPresenter implements UserContract.Presenter {

    private final UserContract.Model mModel;
    private final UserContract.View mView;

    public UserPresenter(UserContract.Model model, UserContract.View view) {
        mModel = model;
        mView = view;
        mView.setPresenter(this);
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

    @Override
    public void start() {
        loadUser();
    }
}
