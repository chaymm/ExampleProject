package com.example.exampleproject.base.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.exampleproject.R;


/**
 * 带刷新功能Activity基类
 * Created by Administrator on 2017/5/23.
 */

public abstract class BaseRefreshViewActivity extends BaseActivity {

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void getBundleData(Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_refreshview;
    }

    @Override
    protected void initWidget() {
        mSwipeRefreshLayout = getViewById(R.id.swipeRefreshLayout);
    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化刷新控件
     */
    protected void initSwipeRefresh() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
    }

    /**
     * 刷新操作
     */
    protected abstract void onRefreshData();
}
