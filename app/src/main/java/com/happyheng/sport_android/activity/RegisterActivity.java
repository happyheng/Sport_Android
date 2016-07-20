package com.happyheng.sport_android.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.happyheng.sport_android.R;
import com.happyheng.sport_android.model.request.BaseRequest;
import com.happyheng.sport_android.model.request.RegisterRequest;

/**
 * "注册"的Activity
 * Created by liuheng on 16/6/7.
 */
public class RegisterActivity extends BaseActivity {

    private EditText mUserNameEt, mPassWordEt, mPassWordConfirmEt, mNickNameEt;
    private Button mRegisterBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        mUserNameEt = (EditText) findViewById(R.id.username_et);
        mPassWordEt = (EditText) findViewById(R.id.password_et);
        mPassWordConfirmEt = (EditText) findViewById(R.id.password_confirm_et);
        mNickNameEt = (EditText) findViewById(R.id.nickname_et);

        mRegisterBt = (Button) findViewById(R.id.register_bt);
        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = mUserNameEt.getText().toString();
                String passWord = mPassWordEt.getText().toString();
                String passWordConfirm = mPassWordConfirmEt.getText().toString();
                String nickName = mNickNameEt.getText().toString();

                //进行判断，如果为空则弹出提示
                if (TextUtils.isEmpty(userName)) {
                    showToast(getString(R.string.user_empty));
                } else if (TextUtils.isEmpty(passWord) || TextUtils.isEmpty(passWordConfirm)) {
                    showToast(getString(R.string.pass_empty));
                } else if (!passWord.equals(passWordConfirm)) {
                    showToast(getString(R.string.pass_wrong_confirm));
                } else if (TextUtils.isEmpty(nickName)) {
                    showToast(getString(R.string.nick_empty));
                }

                BaseRequest request = new RegisterRequest(userName, passWord, nickName, new RegisterRequest.onRegisterListener() {
                    @Override
                    public void onSuccess() {
                        showToast(getString(R.string.register_success));
                        finish();
                    }

                    @Override
                    public void onRepeatUserName() {
                        showToast(getString(R.string.repeat_user_name));
                    }

                    @Override
                    public void onRepeatNickName() {
                        showToast(getString(R.string.repeat_nick));
                    }

                    @Override
                    public void onFail() {
                        showToast(getString(R.string.wrong_request));
                    }
                });
                request.doRequest();
            }
        });
    }
}
