package com.example.exampleproject.base.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.exampleproject.R;
import com.example.exampleproject.base.adapter.BaseItemDecoration;
import com.example.exampleproject.base.adapter.BaseRecyclerAdapter;
import com.example.exampleproject.base.view.pullrecyclerview.PullRecyclerView;
import com.example.exampleproject.base.view.pullrecyclerview.layoutmanager.ILayoutManager;


/**
 * 上拉刷新下拉更多RecylerView Activity基类
 * Created by chang on 2017/2/28.
 */

public abstract class BaseRecyclerViewActivity extends BaseActivity implements BaseRecyclerAdapter.OnRecyclerItemClickListener, BaseRecyclerAdapter.OnRecyclerItemLongClickListener {

    protected PullRecyclerView mPullToRefreshRecyclerView;
    protected BaseRecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_recylerview;
    }

    @Override
    protected void initWindow() {

    }

    @Override
    protected void initWidget() {
        mPullToRefreshRecyclerView = getViewById(R.id.recyclerView);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onItemClick(View view, int position) {
        onRecyclerViewItemClick(view, position);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        onRecyclerViewItemLongClick(view, position);
        return true;
    }

    /**
     * 设置适配器
     */
    protected void setAdapter() {
//        if (mAdapter != null) {
//            mAdapter.notifyDataSetChanged();
//            return;
//        }
        mPullToRefreshRecyclerView.setColorSchemeResources(R.color.colorPrimary);
        mPullToRefreshRecyclerView.setLayoutManager(getLayoutManager());
        if (getItemDecoration() == null) {
            mPullToRefreshRecyclerView.addItemDecoration(new BaseItemDecoration(this,BaseItemDecoration.VERTICAL_LIST,false));
        } else {
            mPullToRefreshRecyclerView.addItemDecoration(getItemDecoration());
        }
        mPullToRefreshRecyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                onRefresh();
            }

            @Override
            public void onLoadMore() {
                onLoadMore();
            }
        });
        mAdapter = getRecyclerAdapter();
        mAdapter.setOnRecyclerItemClickListener(this);
        mAdapter.setOnRecyclerItemLongClickListener(this);
        mPullToRefreshRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 获取布局管理器
     *
     * @return
     */
    protected abstract ILayoutManager getLayoutManager();

    /**
     * 获取列表项分割线
     *
     * @return
     */
    protected abstract RecyclerView.ItemDecoration getItemDecoration();

    /**
     * 获取适配器
     *
     * @return
     */
    protected abstract BaseRecyclerAdapter getRecyclerAdapter();

    /**
     * 刷新操作
     */
    protected abstract void onRefresh();

    /**
     * 加载更多
     */
    protected abstract void onLoadMore();

    /**
     * 单击选项
     *
     * @param view     视图
     * @param position 列表位置
     */
    protected abstract void onRecyclerViewItemClick(View view, int position);

    /**
     * 长按选项
     *
     * @param view     视图
     * @param position 列表位置
     */
    protected abstract void onRecyclerViewItemLongClick(View view, int position);

}
