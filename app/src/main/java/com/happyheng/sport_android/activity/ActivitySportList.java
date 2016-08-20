package com.happyheng.sport_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.happyheng.sport_android.R;
import com.happyheng.sport_android.model.User;
import com.happyheng.sport_android.model.request.SportListRequest;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示所有运动记录的Activity
 * Created by liuheng on 16/8/15.
 */
public class ActivitySportList extends BaseActivity implements SportListRequest.ListListener,ListView.OnItemClickListener{

    private ListView mSportList;
    private SportListAdapter mAdapter;
    private List<Integer> mListData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_list);

        initView();
        initData();
    }

    private void initView() {
        mSportList = (ListView) findViewById(R.id.sport_list_lv);
        mAdapter = new SportListAdapter();
        mSportList.setAdapter(mAdapter);
        mSportList.setOnItemClickListener(this);
    }

    private void initData() {
        SportListRequest request = new SportListRequest(User.getUser().getUserToken());
        request.setListListener(this);
        request.doRequest();
    }

    @Override
    public void onSuccess(List<Integer> list) {
        mListData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFail() {
        Logger.d("获取失败");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int sportId = mListData.get(position);
        Logger.d("得到的sportId为"+sportId);

        Intent intent = new Intent(this,ActivitySportMessage.class);
        intent.putExtra(ActivitySportMessage.TAG_SPORT_ID,sportId);
        startActivity(intent);
    }

    private class SportListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = LayoutInflater.from(ActivitySportList.this).inflate(R.layout.item_sport_list,null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.item_sport_list_tv);
            textView.setText("运动轨迹"+(position+1));

            return convertView;
        }
    }
}
