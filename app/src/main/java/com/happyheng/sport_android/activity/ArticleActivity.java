package com.happyheng.sport_android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.happyheng.sport_android.R;
import com.orhanobut.logger.Logger;

/**
 * 文章详情界面
 * Created by liuheng on 16/7/27.
 */
public class ArticleActivity extends BaseActivity {

    public static final String BUNDLE_URL = "url";
    //private static final String TEST_URL = "http://192.168.0.104:8080/Sport/NewsDetail?id=3";

    private WebView mWebView;

    //文章的url
    private String mArticleUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        mWebView = (WebView) findViewById(R.id.article_wv);

        initWebView();

        mArticleUrl = getIntent().getStringExtra(BUNDLE_URL);

        mWebView.loadUrl(mArticleUrl);
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
    }
}
