package com.ysx.androidmvp.user;

/**
 * @author ysx
 * @date 2017/11/28
 * @description UserPresenter单元测试
 * 参考：https://github.com/googlesamples/android-architecture/blob/todo-mvp/todoapp/app/src/test/java/com/example/android/architecture/blueprints/todoapp/addedittask/AddEditTaskPresenterTest.java
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link UserPresenter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserPresenterTest {

    @Mock
    private UserModel mMockUserModel;
    @Mock
    private UserContract.View mMockView;

    private UserPresenter mUserPresenter;

    @Test
    public void loadUser_Success() {
        mUserPresenter = new UserPresenter(mMockView);

        mUserPresenter.loadUser();

        UserBean userBean = verify(mMockUserModel).loadUser();
        verify(mMockView).setName(userBean.getName());
        verify(mMockView).setAge(userBean.getAge());
    }


}
