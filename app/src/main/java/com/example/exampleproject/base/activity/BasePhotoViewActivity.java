package com.example.exampleproject.base.activity;


import android.support.v4.view.ViewPager;

import com.example.exampleproject.R;
import com.example.exampleproject.base.adapter.BasePagerAdapter;
import com.example.exampleproject.base.view.HackyViewPager;


/**
 * 左右滑动并手势缩放图片Activity基类
 * Created by chang on 2017/3/1.
 */

public abstract class BasePhotoViewActivity extends BaseActivity {

    protected HackyViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_photoview;
    }

    @Override
    protected void initWindow() {

    }

    @Override
    protected void initWidget() {
        mViewPager = getViewById(R.id.viewPager_photoView);
    }

    @Override
    protected void initData() {
        mViewPager.setAdapter(getAdapter());
        mViewPager.setCurrentItem(getAdapter().getCurrentPosition());
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
     * 获取ViewPager适配器基类
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
