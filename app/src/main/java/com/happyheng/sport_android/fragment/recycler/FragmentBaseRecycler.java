package com.happyheng.sport_android.fragment.recycler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happyheng.sport_android.R;


/**
 * 下拉刷新使用的RecyclerView的父类
 */
public abstract class FragmentBaseRecycler
        extends Fragment
{
    //默认还有多少item到底部时需要加载更多
    private static final int DEFAULT_RESET_ITEM_COUNT = 3;

    private View mContentView;
    private SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;

    private RecyclerView.OnScrollListener mScrollListener = new ScrollListener();
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new RefreshListener();

    protected boolean mIsInit = true;
    //是否正在请求的标志位
    private boolean mIsLoad = false;
    //是否有更多的标志位
    protected boolean mShouldLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        if (mContentView == null)
        {
            mContentView = inflater.inflate(R.layout.fragment_base_recycler, container, false);
            mRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.refresh_layout);
            mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recycler_view);

            //设置是否下拉刷新
            mRefreshLayout.setEnabled(isRefreshEnable());
            if (isRefreshEnable())
            {
                mRefreshLayout.setRefreshing(true);
            }
            mRefreshLayout.setOnRefreshListener(mRefreshListener);
            mRecyclerView.addOnScrollListener(mScrollListener);

            //刚开始进入时，要进行刷新操作
            load(true);
        }
        else
        {
            mIsInit = false;
        }
        return mContentView;
    }


    //进行刷新的方法
    protected void refresh(boolean isShowSwipeRefreshView){
        if (isShowSwipeRefreshView){
            mRefreshLayout.setRefreshing(true);
        }
        load(true);
    }

    //从开始加载，或者从后面进行加载的方法
    protected void load(boolean isRefresh)
    {
        if (!mIsLoad)
        {
            LoadAsyncTask loadTask = new LoadAsyncTask(isRefresh);
            loadTask.execute();
        }
    }


    //SwipeRefreshLayout刷新的实现类
    private class RefreshListener
            implements SwipeRefreshLayout.OnRefreshListener
    {

        @Override
        public void onRefresh()
        {
            if (mIsLoad)
            {
                mRefreshLayout.setRefreshing(false);
            }
            else
            {
                load(true);
            }

        }
    }

    //RecyclerView滑动时回调的实现类
    private class ScrollListener
            extends RecyclerView.OnScrollListener
    {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            int lastVisibleItem = findLastVisibleItemPosition();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            //当距离底部小于约定数且有下一页时，才会进行刷新
            if (totalItemCount - lastVisibleItem <= DEFAULT_RESET_ITEM_COUNT && mShouldLoad)
            {
                load(false);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState)
        {

        }
    }

    /**
     * 设置SwipeRefreshLayout下拉刷新时结束的位置
     */
    protected void setSwipeRefreshEnd(int end){
        setSwipeRefreshStartAndEnd(0,end);
    }

    /**
     * 设置SwipeRefreshLayout下拉刷新时开始的位置和结束的位置
     */
    protected void setSwipeRefreshStartAndEnd(int start,int end){
        mRefreshLayout.setProgressViewOffset(false,start,end);
    }

    //每次都进行加载的AsyncTask
    protected class LoadAsyncTask
            extends AsyncTask<Void, Void, RequestResult>
    {

        private boolean mIsRefresh;

        public LoadAsyncTask(boolean isRefresh)
        {
            this.mIsRefresh = isRefresh;
        }


        @Override
        protected RequestResult doInBackground(Void... params)
        {
            mIsLoad = true;
            return requestDataInBackground(mIsRefresh);
        }

        @Override
        protected void onPostExecute(RequestResult requestResult)
        {

            mIsLoad = false;

            //将mRefreshLayout消失　
            mRefreshLayout.setRefreshing(false);

            if (requestResult != null && requestResult.ret == RequestResult.RET_SUCCESS && requestResult.addCount != 0)
            {
                mShouldLoad = true;
            }
            else
            {
                mShouldLoad = false;
            }

            //这个应该子类去实现
            onPostRequest(requestResult, mIsRefresh);
        }
    }

    public class RequestResult
    {
        public static final int RET_SUCCESS = 0;
        public static final int RET_FAILED = -1;
        public static final int ADD_COUNT_EMPTY = 0;

        public int ret = RET_FAILED;
        public int addCount = ADD_COUNT_EMPTY;
    }


    //子类需要实现的方法
    protected abstract RequestResult requestDataInBackground(boolean isRefresh);

    protected abstract void onPostRequest(RequestResult data, boolean refresh);

    protected abstract int findLastVisibleItemPosition();


    //子类来控制父类行为的方法
    // 是否可以下拉刷新
    protected boolean isRefreshEnable()
    {
        return true;
    }

}
