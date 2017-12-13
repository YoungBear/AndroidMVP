package com.ysx.androidmvp.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author ysx
 * @date 2017/12/13
 * @description UserPresenter单元测试
 * 参考：https://github.com/googlesamples/android-architecture/blob/todo-mvp/todoapp/app/src/test/java/com/example/android/architecture/blueprints/todoapp/addedittask/AddEditTaskPresenterTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class UserPresenterTest {

    private static final String TEST_NAME = "Test name";
    private static final int TEST_AGE = 20;

    private UserBean mUserBean;

    @Mock
    private UserModel mMockUserModel;
    @Mock
    private UserContract.View mMockView;

    private UserPresenter mUserPresenter;

    /**
     * UserPresenter的构造方法
     * 验证调用了setPresenter函数
     */
    @Test
    public void createPresenter_setsThePresenterToView() {
        mUserPresenter = new UserPresenter(mMockUserModel, mMockView);
        verify(mMockView).setPresenter(mUserPresenter);
    }


    /**
     * loadUser方法
     * 1. 验证调用了UserModel的loadUser方法
     * 2. 验证调用了View的setName和setAge方法
     */
    @Test
    public void loadUser_Success() {
        mUserBean = new UserBean();
        mUserBean.setName(TEST_NAME);
        mUserBean.setAge(TEST_AGE);
        when(mMockUserModel.loadUser()).thenReturn(mUserBean);
        mUserPresenter = new UserPresenter(mMockUserModel, mMockView);

        mUserPresenter.loadUser();

        verify(mMockUserModel).loadUser();

        verify(mMockView).setName(mUserBean.getName());
        verify(mMockView).setAge(mUserBean.getAge());
    }

    /**
     * saveUser方法
     * 验证调用了UserModel的saveUser方法，传递任意的UserBean对象
     */
    @Test
    public void saveUser_Success() {
        mUserPresenter = new UserPresenter(mMockUserModel, mMockView);
        mUserPresenter.saveUser("testName", 21);
        verify(mMockUserModel).saveUser(any(UserBean.class));
    }
}
