package com.happyheng.sport_android.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.happyheng.sport_android.activity.ArticleActivity;
import com.happyheng.sport_android.fragment.recycler.FragmentLoadRecycler;
import com.happyheng.sport_android.model.News;
import com.happyheng.sport_android.model.request.NewsRequest;
import com.happyheng.sport_android.view.NewsItemView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面---展示信息的Fragment
 * Created by liuheng on 16/7/14.
 */
public class FragmentNews extends FragmentLoadRecycler {

    private List<News> mNewsList = new ArrayList<>();

    private void OnItemClick(int position) {
        News news = mNewsList.get(position);
        String articleUrl = news.getUrl();

        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra(ArticleActivity.BUNDLE_URL, articleUrl);
        startActivity(intent);
    }

    @Override
    protected RequestResult requestDataInBackground(boolean isRefresh) {

        int begin;
        if (isRefresh) {
            begin = 0;
        } else {
            begin = mNewsList.size();
        }

        NewsRequest request = new NewsRequest();
        request.setBeginCount(begin, DEFAULT_REQUEST_COUNT);
        List<News> resultList = request.getNewsItem();

        RequestResult result = new RequestResult();
        if (resultList != null) {
            mNewsList.addAll(resultList);

            result.ret = RequestResult.RET_SUCCESS;
            result.addCount = resultList.size();
        } else {
            result.ret = RequestResult.RET_FAILED;
        }
        return result;
    }

    @Override
    protected int getItemSize() {
        return mNewsList.size();
    }

    @Override
    protected void setItemOffset(Rect outRect, int position) {
        outRect.set(0, mDefaultTopDecoration, 0, 0);
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        NewsItemView view = new NewsItemView(getContext(), null);
        return getBaseViewHolder(view);
    }

    @Override
    protected void bindingViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NewsItemView itemView = (NewsItemView) holder.itemView;
        News news = mNewsList.get(position);

        itemView.setThumbnailSource(news.getThumbnail());
        itemView.setTitleString(news.getName());
        itemView.setContentString(news.getSimplecontent());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemClick(position);
            }
        });
    }

}

