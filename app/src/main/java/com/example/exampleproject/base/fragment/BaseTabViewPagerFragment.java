package com.example.exampleproject.base.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.exampleproject.R;
import com.example.exampleproject.base.adapter.BaseFragmentPagerAdapter;
import com.example.exampleproject.base.adapter.BasePagerInfo;

import java.util.List;


/**
 * 带Tab和ViewPager Fragment基类
 * Created by chang on 2017/2/7.
 */
public abstract class BaseTabViewPagerFragment extends BaseFragment {

    protected TabLayout mTabNav;
    protected ViewPager mViewPager;
    private BaseFragmentPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_tab_viewpager;
    }

    @Override
    protected void initWidget(View root) {
        mTabNav = getViewById(R.id.tabLayout);
        mViewPager = getViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
        mAdapter = new BaseFragmentPagerAdapter(getActivity(), getChildFragmentManager(), getPagers());
        mViewPager.setAdapter(mAdapter);
        mTabNav.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0, true);
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

    protected Fragment getFragment(){
        return mAdapter.getCurFragment();
    }

    /**
     * 获取ViewPager PagerInfo集合
     * @return
     */
    protected abstract List<BasePagerInfo> getPagers();

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
