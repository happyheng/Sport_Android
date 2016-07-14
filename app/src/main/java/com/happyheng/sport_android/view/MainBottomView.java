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
public class MainBottomView extends LinearLayout {

    //默认选择的位置
    private static final int DEFAULT_POSITION = 0;
    private static final int TAB_NUM = 3;

    private final int[] UNSELECTED_IMAGE_BG = new int[]{R.mipmap.tab_information_unselected,R.mipmap.tab_sport_unselected,R.mipmap.tab_me_unselected};
    private final int[] SELECTED_IMAGE_BG = new int[]{R.mipmap.tab_information_selected,R.mipmap.tab_sport_selected,R.mipmap.tab_me_selected};

    private View mInformationTab, mSportTab, mMineTab;
    private ImageView mInformationIv,mSportIv,mMineIv;
    private ImageView[] mImages = new ImageView[TAB_NUM];

    private View.OnClickListener mClickListener = new ClickListener();

    private int mSelectIndex = DEFAULT_POSITION;

    public MainBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_main_bottom, this, true);

        mInformationTab = findViewById(R.id.tab_information);
        mSportTab = findViewById(R.id.tab_sport);
        mMineTab = findViewById(R.id.tab_mine);


        mInformationIv = (ImageView) findViewById(R.id.information_iv);
        mSportIv = (ImageView) findViewById(R.id.sport_iv);
        mMineIv = (ImageView) findViewById(R.id.mine_iv);
        mImages[0] = mInformationIv;
        mImages[1] = mSportIv;
        mImages[2] = mMineIv;

        setImageState(mSelectIndex,true);

        mInformationTab.setOnClickListener(mClickListener);
        mSportTab.setOnClickListener(mClickListener);
        mMineTab.setOnClickListener(mClickListener);
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (mBottomClickListener != null) {

                int newSelect;
                switch (v.getId()) {
                    case R.id.tab_information:
                        newSelect = 0;
                        break;

                    case R.id.tab_sport:
                        newSelect = 1;
                        break;

                    default:
                        newSelect = 2;
                        break;
                }

                if (mSelectIndex != newSelect){
                    setImageState(mSelectIndex,false);
                    setImageState(newSelect,true);

                    mBottomClickListener.onClick(mSelectIndex, newSelect);
                    mSelectIndex = newSelect;
                }

            }

        }
    }

    private void setImageState(int position,boolean isSelected){
        if (isSelected){
            mImages[position].setBackgroundDrawable(getResources().getDrawable(SELECTED_IMAGE_BG[position]));
        } else {
            mImages[position].setBackgroundDrawable(getResources().getDrawable(UNSELECTED_IMAGE_BG[position]));
        }
    }


    public interface OnBottomChooseListener {
        void onClick(int oldPositon,int position);
    }

    private OnBottomChooseListener mBottomClickListener;

    public void setBottomCLickListener(OnBottomChooseListener bottomCLickListener) {
        this.mBottomClickListener = bottomCLickListener;
    }

}
