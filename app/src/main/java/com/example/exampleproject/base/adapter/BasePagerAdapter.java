package com.example.exampleproject.base.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * PagerAdapter基类
 * Created by chang on 2017/2/9.
 */

public class BasePagerAdapter<T> extends PagerAdapter {

    protected List<T> mList;

    public int mCurrentPosition = 0;

    public BasePagerAdapter(){}

    public BasePagerAdapter(List<T> list) {
        this.mList = list;
    }

    public void setList(List<T> list){
        this.mList = list;
    }

    public void setCurrentPosition(int position){
        this.mCurrentPosition = position;
    }

    public int getCurrentPosition(){
        return mCurrentPosition;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
