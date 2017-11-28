# Android MVP 代码练习 - UserActivity

功能描述：

UserActivity只有一个界面，用来显示用户名，年龄，还有一些设置按钮。第一次进入的时候，默认从本地SharedPreferences加载数据，如果没有，则显示默认值("defalusName", 0)。

用户可以编辑这两个属性，并且点击**确定**按钮后，新的值会存储到SharedPreferences中，覆盖旧值。或者用户可以点击**重置**按钮，就会从SharedPreferences获取最新的数据并显示。

![UserActivity](http://img.blog.csdn.net/20171125142342867?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTmV4dF9TZWNvbmQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

开始写代码

首先应该创建UserBean类，详细代码可以通过[Github][1]地址的工程里边找到。

## 1. 编写契约类

准确的说是契约接口。由于MVP都是面向接口编程的，我们新建一个契约(Contract)接口来统一管理这些接口。即 Model，View 和 Presenter 接口。

首先，我们需要思考下，MVP 三个部分各自的功能，然后写出对应的方法来。

<font color=red>**Model**</font>，我们需要实现两个具体功能：

- <font color=red>**加载用户数据**</font>
- <font color=red>**保存用户数据**</font>

具体代码为：

```
    interface Model {
        UserBean loadUser();
        boolean saveUser(UserBean userBean);
    }
```

<font color=red>**View**</font>，View只实现和UI相关的方法，我们需要实现四个具体功能：

- <font color=red>**获取EditText输入的名字**</font>
- <font color=red>**获取EditTest输入的年龄**</font>
- <font color=red>**设置EditText显示的名字**</font>
- <font color=red>**设置EditText显示的年龄**</font>

具体代码为：

```
    interface View {
        String getInputName();
        int getInputAge();
        void setName(String name);
        void setAge(int age);
    }
```

<font color=red>**Presenter**</font>，由于Presenter是Model和View之间的桥梁，所以我们需要实现两个具体功能：

- <font color=red>**加载用户数据**</font>
- <font color=red>**保存用户数据**</font>

具体代码为：

```
    interface Presenter {
        void loadUser();
        boolean saveUser(String name, int age);
    }
```

最后，把MVP这三个接口统一用一个契约接口来保存，最终的代码为：

```
package com.ysx.androidmvp.user;

/**
 * @author ysx
 * @date 2017/11/25
 * @description User 契约接口，用来存放User相关的Model，View 和 Presenter的接口
 */

public interface UserContract {


    interface Model {
        /**
         * 从SharedPreferences中获取UserBean对象
         * @return 取得的对象
         */
        UserBean loadUser();

        /**
         * 将用户信息保存到SharedPreferences中
         * @param userBean
         * @return 是否成功保存
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
         * 更新UI，更新显示姓名
         * @param name
         */
        void setName(String name);

        /**
         * 更新UI，更新显示年龄
         * @param age
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
```

## 2. 编写具体实现类

根据第1步写的接口，我们可以写出来具体的实现类来。

### 2.1 UserModel

UserModel主要实现加载用户信息和保存用户信息两个方法，通过访问`SharedPreferences`来实现。

```
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
```

需要注意的是，我们需要自定义一个Application类`MyApplication`, 在应用初始化的时候，初始化MyApplication的静态变量`sContext`，以便我们在加载数据的时候使用。详细代码可以通过[Github][1]地址的工程里边找到。

### 2.2 UserPresenter

UserPresenter实现加载用户信息和保存用户信息。Presenter中有两个成员变量，即Model和View，其中<font color=red>**Model用来具体操作数据**</font>，在构造函数中new获得；<font color=red>**View是具体更新UI的**</font>，在构造方法中传递，其具体实现类是`Activity`或者`Fragment`，下边会讲到。

```
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
```

### 2.3 View实现类

View是通过`Activity`或者`Fragment`来实现的，用来具体更新UI。这里使用`Activity`。

`Activity`只要实现更新UI的相关方法即可，再添加按钮的事件监听，分别调用Presenter的save()和load()函数。

```
package com.ysx.androidmvp.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ysx.androidmvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity implements UserContract.View {

    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_age)
    EditText mEtAge;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.btn_reset)
    Button mBtnReset;

    private UserContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        /**
         * 创建一个Presenter对象
         */
        mPresenter = new UserPresenter(this);
    }

    /**
     * 在onResume()函数中加载新数据并更新UI
     */
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadUser();
    }

    @Override
    public String getInputName() {
        return mEtName.getText().toString();
    }

    @Override
    public int getInputAge() {
        return Integer.valueOf(mEtAge.getText().toString());
    }

    @Override
    public void setName(String name) {
        mEtName.setText(name);
    }

    @Override
    public void setAge(int age) {
        mEtAge.setText(String.valueOf(age));
    }

    @OnClick({R.id.btn_confirm, R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                boolean isSuccess = mPresenter.saveUser(getInputName(), getInputAge());
                if (isSuccess) {
                    Toast.makeText(UserActivity.this, R.string.tip_save_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, R.string.tip_save_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_reset:
                mPresenter.loadUser();
                break;
            default:
                break;
        }
    }
}

```

## 添加单元测试

参考google-sample的单元测试项目[Basic sample for Espresso](https://github.com/googlesamples/android-testing/tree/master/ui/espresso/BasicSample)。

### 测试UserModel

这里参考[BasicSample](https://github.com/googlesamples/android-testing/tree/master/ui/espresso/BasicSample)中的`SharedPreferencesHelperTest`测试类。

UserModel有两个方法：`loadUser()和saveUser()`。所以我们只需要测试加载用户和保存用户即可。这里，我们写一个测试的用户：name为"Test name"，age为20。我们的测试步骤是，首先将该用户调用loadUser()保存，然后再读取用户，判断name和age字段的值是否相等。详细代码为：

```
package com.ysx.androidmvp;

import android.content.SharedPreferences;

import com.ysx.androidmvp.user.UserBean;
import com.ysx.androidmvp.user.UserModel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ysx
 * @date 2017/11/28
 * @description UserModel的单元测试
 */

public class UserModelTest {
    private static final String TEST_NAME = "Test name";
    private static final int TEST_AGE = 20;

    private UserBean mUserBean;
    private UserModel mMockUserModel;
    private UserModel mMockBrokenUserModel;

    /**
     * Mock对象
     */
    private SharedPreferences mMockSharedPreferences;
    private SharedPreferences mMockBrokenSharedPreferences;
    private SharedPreferences.Editor mMockEditor;
    private SharedPreferences.Editor mMockBrokenEditor;

    @Before
    public void initMocks() {

        mMockSharedPreferences = mock(SharedPreferences.class);
        mMockBrokenSharedPreferences = mock(SharedPreferences.class);
        mMockEditor = mock(SharedPreferences.Editor.class);
        mMockBrokenEditor = mock(SharedPreferences.Editor.class);

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
```






## [Demo源代码地址][1]


[1]: https://github.com/YoungBear/AndroidMVP