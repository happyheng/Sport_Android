package com.happyheng.sport_android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.happyheng.sport_android.R;

/**
 * 主页顶部的View
 * Created by liuheng on 16/7/12.
 */
public class MainBottomView extends LinearLayout{

    private View mInformationTab,mSportTab,mMineTab;
    private ImageView mInformationIv,mSportIv,mMineIv;

    public MainBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_main_bottom,this,true);

        mInformationTab = findViewById(R.id.tab_information);
        mSportTab = findViewById(R.id.tab_sport);
        mMineTab = findViewById(R.id.tab_mine);

        mInformationIv = (ImageView) findViewById(R.id.information_iv);
        mSportIv = (ImageView) findViewById(R.id.sport_iv);
        mMineIv = (ImageView) findViewById(R.id.mine_iv);
    }



}
