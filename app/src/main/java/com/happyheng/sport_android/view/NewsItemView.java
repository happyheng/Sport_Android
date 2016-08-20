package com.happyheng.sport_android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.happyheng.sport_android.R;
import com.orhanobut.logger.Logger;

/**
 *
 * Created by liuheng on 16/7/24.
 */
public class NewsItemView extends LinearLayout {

    //顶部的缩略图
    private ImageView mThumbnailIv;
    private TextView mTitleTv, mContentTv;

    public NewsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_news, this, true);

        mThumbnailIv = (ImageView) findViewById(R.id.item_thumbnail_iv);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mContentTv = (TextView) findViewById(R.id.content_tv);
    }

    public void setThumbnailSource(String url) {
        Glide.with(getContext()).load(url).into(mThumbnailIv);
    }

    public void setTitleString(String title) {
        mTitleTv.setText(title);
    }

    public void setContentString(String content) {
        mContentTv.setText(content);
    }
}
