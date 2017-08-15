package com.example.exampleproject.base.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.exampleproject.R;
import com.example.exampleproject.base.adapter.BasePagerAdapter;


/**
 * 带ViewPager Fragment基类
 * Created by chang on 2017/3/1.
 */

public abstract class BaseViewPagerFragment extends BaseFragment {

    protected ViewPager mViewPager;

    @Override
    protected void getBundleData(Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_viewpager;
    }

    @Override
    protected void initWidget(View root) {
        mViewPager = getViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
        mViewPager.setAdapter(getAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onViewPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                onViewPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                onViewPageScrollStateChanged(state);
            }
        });
    }

    /**
     * 获取PagerAdapter
     *
     * @return
     */
    protected abstract BasePagerAdapter getAdapter();

    /**
     * ViewPager onPageScrolled
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    protected abstract void onViewPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    /**
     * ViewPager onPageSelected
     *
     * @param position
     */
    protected abstract void onViewPageSelected(int position);

    /**
     * ViewPager onPageScrollStateChanged
     *
     * @param state
     */
    protected abstract void onViewPageScrollStateChanged(int state);
}
