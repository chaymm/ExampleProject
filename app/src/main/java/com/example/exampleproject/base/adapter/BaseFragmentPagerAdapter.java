package com.example.exampleproject.base.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * FragmentPager适配器基类
 * Created by chang on 2017/2/27.
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<BasePagerInfo> mInfoList;
    private Fragment mCurFragment;

    public BaseFragmentPagerAdapter(Context context, FragmentManager fm, List<BasePagerInfo> infoList) {
        super(fm);
        mContext = context;
        mInfoList = infoList;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object instanceof Fragment) {
            mCurFragment = (Fragment) object;
        }
    }

    public Fragment getCurFragment() {
        return mCurFragment;
    }

    @Override
    public Fragment getItem(int position) {
        BasePagerInfo info = mInfoList.get(position);
        return Fragment.instantiate(mContext, info.getClx().getName(), info.getArgs());
    }

    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mInfoList.get(position).getTitle();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

}
