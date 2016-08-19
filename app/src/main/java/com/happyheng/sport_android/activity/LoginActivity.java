package com.happyheng.sport_android.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.happyheng.sport_android.R;
import com.happyheng.sport_android.model.request.BaseRequest;
import com.happyheng.sport_android.model.request.LoginRequest;
import com.happyheng.sport_android.model.User;
import com.happyheng.sport_android.utils.DensityUtils;
import com.happyheng.sport_android.utils.ScreenUtils;
import com.happyheng.sport_android.utils.SimpleStartActivityUtils;
import com.happyheng.sport_android.utils.ToastUtils;
import com.orhanobut.logger.Logger;

/**
 * "登陆"的Activity
 * Created by liuheng on 16/6/6.
 */
public class LoginActivity extends BaseActivity {

    //动画的时长
    private static final int ANIMATION_TIME = 1000;
    //底部Button距中间线的高度
    private static final int BOTTOM_MARGIN = 50;

    private View.OnClickListener mLoginBtClickListener = new OnLoginBtClickListener();
    private LoginRequest.OnLoginListener  mLoginListener = new OnLoginListener();

    private ImageView mIconIv;
    private EditText mUserNameEt, mPassWordEt;
    private Button mLoginBt, mRegisterBt;

    private View mTopLl, mBottomLl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        beginAnimation();
    }


    /**
     * 初始化所有的View
     */
    private void initView() {
        mIconIv = (ImageView) findViewById(R.id.icon_iv);
        mUserNameEt = (EditText) findViewById(R.id.username_et);
        mPassWordEt = (EditText) findViewById(R.id.password_et);
        mLoginBt = (Button) findViewById(R.id.login_bt);
        mRegisterBt = (Button) findViewById(R.id.register_bt);

        mTopLl = findViewById(R.id.top_ll);
        mBottomLl = findViewById(R.id.bottom_ll);

        mLoginBt.setOnClickListener(mLoginBtClickListener);

        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击注册，跳转到注册的界面
                SimpleStartActivityUtils.startActivity(LoginActivity.this, RegisterActivity.class);
            }
        });
    }

    private class OnLoginBtClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String userNameString = mUserNameEt.getText().toString();
            String passWordString = mPassWordEt.getText().toString();
            //1、先判断是否为空
            if (TextUtils.isEmpty(userNameString) || TextUtils.isEmpty(passWordString)) {
                ToastUtils.showToast(LoginActivity.this, getResources().getString(R.string.user_pass_empty));
            }
            //2、判断是否为用户名为字母+数字


            //3、调用相关接口
            BaseRequest request = new LoginRequest(userNameString, passWordString, mLoginListener);
            request.doRequest();
        }
    }

    private class OnLoginListener implements LoginRequest.OnLoginListener{

        @Override
        public void onSuccess(String token) {

            User.getUser().setUserToken(token);

            Intent intent = new Intent(LoginActivity.this, ActivityMain.class);
            startActivity(intent);
        }


        @Override
        public void onFail(int code) {
            Logger.d("失败的code为" + code);

            switch (code) {

                case LoginRequest.REQUEST_WRONG_USERNAME:
                    ToastUtils.showToast(LoginActivity.this, getResources().getString(R.string.wrong_username));

                    break;

                case LoginRequest.REQUEST_WRONG_PASSWORD:
                    ToastUtils.showToast(LoginActivity.this, getResources().getString(R.string.wrong_password));

                    break;

                default:
                    ToastUtils.showToast(LoginActivity.this, getResources().getString(R.string.wrong_request));

                    break;
            }
        }
    }

    /**
     * 开始动画
     */
    private void beginAnimation() {

        final int screenHeight = ScreenUtils.getScreenHeight(this);

        mTopLl.post(new Runnable() {
            @Override
            public void run() {
                int topLlHeight = mTopLl.getMeasuredHeight();
                int animationHeight = screenHeight / 2 - topLlHeight;

                PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("translationY", animationHeight);
                PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1.0f);

                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mTopLl, holder1, holder2);
                animator.setDuration(ANIMATION_TIME);
                animator.start();
            }
        });

        mBottomLl.post(new Runnable() {
            @Override
            public void run() {
                int animationHeight = screenHeight / 2 + DensityUtils.dp2px(LoginActivity.this, BOTTOM_MARGIN) - mBottomLl.getTop();

                PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("translationY", animationHeight);
                PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1.0f);

                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mBottomLl, holder1, holder2);
                animator.setDuration(ANIMATION_TIME);
                animator.start();
            }
        });
    }
}
