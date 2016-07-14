package com.happyheng.sport_android.activity;

import android.os.Bundle;

import com.happyheng.sport_android.R;
import com.happyheng.sport_android.view.MainBottomView;
import com.orhanobut.logger.Logger;

/**
 * 主页面
 * Created by liuheng on 16/7/12.
 */
public class ActivityMain extends BaseActivity{

    private MainBottomView mBottomView;


    private MainBottomView.OnBottomChooseListener mBottomChooseListener = new BottomChooseListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomView = (MainBottomView)findViewById(R.id.main_bottom_tab);
        mBottomView.setBottomCLickListener(mBottomChooseListener);
    }


    private class BottomChooseListener implements MainBottomView.OnBottomChooseListener {

        @Override
        public void onClick(int oldPosition, int position) {
            Logger.d("oldPosition为"+oldPosition+"-----position为"+position);
        }
    }


}
