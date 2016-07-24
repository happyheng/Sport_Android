package com.happyheng.sport_android.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.happyheng.sport_android.R;
import com.happyheng.sport_android.activity.MapActivity;

/**
 * 主页面---运动的Fragment
 * Created by liuheng on 16/7/14.
 */
public class FragmentSport extends Fragment implements View.OnClickListener{

    private Button mSportStartButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport,container,false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSportStartButton = (Button) view.findViewById(R.id.tab_sport_start_bt);

        mSportStartButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //如果是开始运动的按钮
            case R.id.tab_sport_start_bt:

                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                break;

        }
    }
}
