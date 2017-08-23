package com.example.exampleproject.base.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.exampleproject.R;
import com.example.exampleproject.base.adapter.BasePhotoViewAdapter;
import com.example.exampleproject.base.view.XViewPager;


/**
 * 带手势缩放PhotoView Fragment基类
 * Created by chang on 2017/3/1.
 */

public abstract class BasePhotoViewFragment extends BaseFragment {

    protected XViewPager mViewPager;

    @Override
    protected void getBundleData(Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_photoview;
    }

    @Override
    protected void initWidget(View root) {
        mViewPager = getViewById(R.id.viewPager_photoview);
    }

    @Override
    protected void initData() {
        mViewPager.setAdapter(getAdapter());
        mViewPager.setCurrentItem(getAdapter().getCurrentPosition());
    }

    /**
     * 获取PhotoView适配器
     *
     * @return
     */
    protected abstract BasePhotoViewAdapter getAdapter();
}
