package com.happyheng.sport_android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.happyheng.sport_android.R;

/**
 * "注册"的Activity
 * Created by liuheng on 16/6/7.
 */
public class RegisterActivity extends Activity {

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

            }
        });
    }
}
