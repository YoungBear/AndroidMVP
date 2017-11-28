package com.ysx.androidmvp.user;

import android.content.SharedPreferences;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author ysx
 * @date 2017/11/28
 * @description UserModel的单元测试
 */
/**
 * Unit tests for the {@link UserModel} that mocks {@link SharedPreferences}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserModelTest {
    private static final String TEST_NAME = "Test name";
    private static final int TEST_AGE = 20;

    private UserBean mUserBean;
    private UserModel mMockUserModel;
    private UserModel mMockBrokenUserModel;

    /**
     * Mock对象
     */
    @Mock
    private SharedPreferences mMockSharedPreferences;
    @Mock
    private SharedPreferences mMockBrokenSharedPreferences;
    @Mock
    private SharedPreferences.Editor mMockEditor;
    @Mock
    private SharedPreferences.Editor mMockBrokenEditor;

    @Before
    public void initMocks() {

        mUserBean = new UserBean();
        mUserBean.setName(TEST_NAME);
        mUserBean.setAge(TEST_AGE);

        // Create a mocked SharedPreferences.
        mMockUserModel = createMockUserModel();

        // Create a mocked SharedPreferences that fails at saving data.
        mMockBrokenUserModel = createBrokenMockUserModel();

    }

    /**
     * 测试保存和读取用户数据
     */
    @Test
    public void userModel_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences
        boolean success = mMockUserModel.saveUser(mUserBean);

        Assert.assertEquals("Checking that UserModel.save... returns true",
                success, true);

        // Read personal information from SharedPreferences
        UserBean savedUserBean = mMockUserModel.loadUser();

        // Make sure both written and retrieved personal information are equal.
        Assert.assertEquals("Checking that UserModel.name has been persisted and read correctly",
                mUserBean.getName(), savedUserBean.getName());
        Assert.assertEquals("Checking that UserModel.age has been persisted and read correctly",
                mUserBean.getAge(), savedUserBean.getAge());

    }

    @Test
    public void userModel_SavePersonalInformationFailed_ReturnsFalse() {
        // Read personal information from a broken SharedPreferencesHelper
        boolean success =
                mMockBrokenUserModel.saveUser(mUserBean);
        Assert.assertEquals("Makes sure writing to a broken UserModel returns false",
                success, false);
    }

    /**
     * 创建一个mocked的SharedPreferences
     */
    private UserModel createMockUserModel() {
        when(mMockSharedPreferences.getString(
                eq(UserModel.SP_KEY_NAME), anyString()))
                .thenReturn(mUserBean.getName());

        when(mMockSharedPreferences.getInt(
                eq(UserModel.SP_KEY_AGE), anyInt()))
                .thenReturn(mUserBean.getAge());

        // Mocking a successful commit.
        when(mMockEditor.commit()).thenReturn(true);

        // Return the MockEditor when requesting it.
        when(mMockSharedPreferences.edit()).thenReturn(mMockEditor);
        return new UserModel(mMockSharedPreferences);
    }

    /**
     * 创建一个失败的mocked的SharedPreferences
     */
    private UserModel createBrokenMockUserModel() {
        // Return the broken MockEditor when requesting it.
        when(mMockBrokenSharedPreferences.edit()).thenReturn(mMockBrokenEditor);
        return new UserModel(mMockBrokenSharedPreferences);
    }

}
