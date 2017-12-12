package com.ysx.androidmvp.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ysx.androidmvp.MyApplication;
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
        SharedPreferences sharedPreferences = MyApplication.sContext.getSharedPreferences(
                MyApplication.SP_NAME, Context.MODE_PRIVATE);
        UserModel model = new UserModel(sharedPreferences);
        mPresenter = new UserPresenter(model, this);
    }

    /**
     * 在onResume()函数中加载新数据并更新UI
     */
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
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

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = presenter;
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
