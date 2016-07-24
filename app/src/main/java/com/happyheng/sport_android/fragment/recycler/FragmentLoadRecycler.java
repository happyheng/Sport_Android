package com.happyheng.sport_android.fragment.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happyheng.sport_android.R;


/**
 * 继承自FragmentBaseRecycler，此类负责显示Load的判断以及LoadView的显示
 * 也对Adapter和ItemDecoration做出了简化，子类不在需要自定义Adapter，而是实现此类定义好的方法即可
 */
public abstract class FragmentLoadRecycler
        extends FragmentBaseRecycler {

    //默认请求的数量
    protected static final int DEFAULT_REQUEST_COUNT = 10;

    protected LinearLayoutManager mLayoutManager;
    private RecyclerItemDecoration mDecoration;
    protected RecyclerView.Adapter mAdapter;
    //第一种为空View的类型,第二种为item的类型，第三种为footer的类型
    protected static final int TYPE_EMPTY = 0, TYPE_ITEM = 1, TYPE_FOOTER = 2;
    //默认的距离顶部的间距
    protected int mDefaultTopDecoration;

    //是否显示空View,使用此标志位的原因是使其刚开始加载的时候不显示空View，当首次加载成功后，以后才会显示
    protected boolean mIsShowEmptyView = false;
    //是否有默认的背景颜色,使用标志位的原因是子类可以选择不设置默认背景颜色来方式出现的过度绘制的问题
    private boolean mIsHadDefaultBack = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mIsInit) {
            //1、设置分割线
            mDecoration = new RecyclerItemDecoration();
            mRecyclerView.addItemDecoration(mDecoration);

            //2、设置LayoutManager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);

            //3、设置Adapter(子类可以复写这个Adapter)
            mAdapter = new RecyclerAdapter();
            mRecyclerView.setAdapter(mAdapter);

            //得到默认距离顶部的间距
            mDefaultTopDecoration = getResources().getDimensionPixelSize(R.dimen.recycler_default_decoration_height);
            //设置背景颜色
            if (mIsHadDefaultBack) {
                mRecyclerView.setBackgroundColor(getResources().getColor(R.color.recycler_default_bg));
            }
        }

    }

    protected class RecyclerItemDecoration
            extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            setItemOffset(outRect, position);
        }


        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }
    }

    @Override
    protected void onPostRequest(RequestResult data, boolean refresh) {
        mIsShowEmptyView = true;

        notifyDataSetChanged();
    }

    /**
     * 更新RecyclerView的方法
     */
    protected void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    protected RecyclerView.ViewHolder getBaseViewHolder(View itemView) {
        return new RecyclerView.ViewHolder(itemView) {
        };
    }

    public class RecyclerAdapter
            extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            int num = getItemSize();

            //如果没有数据，那么应该显示EmptyView
            if (num == 0 && mIsShowEmptyView) {
                return 1;
            }

            if (mShouldLoad) {
                return num + 1;
            } else {
                return num;
            }
        }

        @Override
        public int getItemViewType(int position) {

            int num = getItemSize();
            if (num == 0) {
                return TYPE_EMPTY;
            }

            if (position == num) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder holder;

            if (viewType == TYPE_EMPTY) {

                View mEmptyView = getEmptyView(parent);
                if (mEmptyView == null) {
                    mEmptyView = getDefaultEmptyView(parent);
                }

                holder = getBaseViewHolder(mEmptyView);
                holder.itemView.setTag(TYPE_EMPTY);
            } else if (viewType == TYPE_ITEM) {
                //从子类中获取对应的Holder
                holder = getViewHolder(parent);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.itemView.setLayoutParams(params);
                holder.itemView.setTag(TYPE_ITEM);
            } else {
                View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_load_more, parent,
                        false);
                footerView.setTag(TYPE_FOOTER);
                holder = getBaseViewHolder(footerView);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.itemView.getTag() != null && (int) holder.itemView.getTag() == TYPE_ITEM) {
                //让子类去绑定数据源
                bindingViewHolder(holder, position);
            }
        }

    }

    /**
     * 得到默认的EmptyView的方法
     *
     * @param parent
     * @return
     */
    protected View getDefaultEmptyView(ViewGroup parent) {
        return new View(getActivity());
    }

    @Override
    protected int findLastVisibleItemPosition() {
        return mLayoutManager.findLastVisibleItemPosition();
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }


    //子类进行配置的方法
    //1、是否画默认的背景
    protected void hadDefaultBackColor(boolean hadDefaultBack) {
        this.mIsHadDefaultBack = hadDefaultBack;
    }


    // 子类必须要实现的方法
    // 1、得到要显示的item的大小（而不是得到数据源的大小，因为有的界面一个item会对应好几个数据源）
    protected abstract int getItemSize();

    // 2、得到item之间的间距
    protected abstract void setItemOffset(Rect outRect, int position);

    // 3、得到子类所对应的Item的ViewHolder
    protected abstract RecyclerView.ViewHolder getViewHolder(ViewGroup parent);

    // 4、设置子类所对应的Item的ViewHolder
    protected abstract void bindingViewHolder(RecyclerView.ViewHolder holder, int position);

    //得到空View，如果想换成其他的空View，子类可以复写这个方法
    public View getEmptyView(ViewGroup parent) {
        return null;
    }

}
