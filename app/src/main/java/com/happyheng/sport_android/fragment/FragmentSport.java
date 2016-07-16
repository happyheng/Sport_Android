package com.happyheng.sport_android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happyheng.sport_android.R;

/**
 * 主页面---运动的Fragment
 * Created by liuheng on 16/7/14.
 */
public class FragmentSport extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport,container,false);
    }
}
