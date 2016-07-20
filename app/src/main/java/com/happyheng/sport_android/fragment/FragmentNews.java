package com.happyheng.sport_android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happyheng.sport_android.R;
import com.happyheng.sport_android.model.News;
import com.happyheng.sport_android.model.request.NewsRequest;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * 主页面---展示信息的Fragment
 * Created by liuheng on 16/7/14.
 */
public class FragmentNews extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        NewsRequest request = new NewsRequest();
        request.setBeginCount(0, 10);
        request.setNewsListener(new RequestNews());
        request.doRequest();
    }

    private class RequestNews implements NewsRequest.OnNewsListener {

        @Override
        public void onSuccess(List<News> list) {
            for (News news : list) {
                Logger.d(news.toString());
            }
        }

        @Override
        public void onFail() {
            Logger.d("请求失败");
        }
    }
}
