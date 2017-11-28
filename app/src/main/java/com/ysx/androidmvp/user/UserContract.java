package com.ysx.androidmvp.user;

/**
 * @author ysx
 * @date 2017/11/25
 * @description User 契约接口，用来存放User相关的Model，View 和 Presenter的接口
 */

public interface UserContract {


    interface Model {
        /**
         * @return 从数据库中获取UserBean对象
         */
        UserBean loadUser();

        /**
         * @param userBean
         * @return 将用户信息保存到本地数据库中
         */
        boolean saveUser(UserBean userBean);
    }

    interface View {

        /**
         * @return 返回从编辑框输入的名字
         */
        String getInputName();

        /**
         * @return 返回从编辑框输入的年龄
         */
        int getInputAge();

        /**
         * @param name 更新UI，更新显示姓名
         */
        void setName(String name);

        /**
         * @param age 更新UI，更新显示年龄
         */
        void setAge(int age);

    }

    interface Presenter {

        /**
         * 加载用户信息
         */
        void loadUser();

        /**
         * @param name
         * @param age
         * @return 是否成功保存
         * 保存用户信息
         */
        boolean saveUser(String name, int age);

    }
}
