package com.example.exampleproject.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListAdapter基类
 * Created by chang on 2017/2/9.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter{
    private List<T> mDatas;
    private List<T> mPreData;

    public BaseListAdapter(Context context, List<T> list) {
        this.mDatas = list;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= 0 && position < mDatas.size())
            return mDatas.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = getItem(position);
        int layoutId = getLayoutId(position, item);
        final BaseViewHolder vh = BaseViewHolder.getViewHolder(convertView, parent, layoutId, position);
        convert(vh, item, position);
        return vh.getConvertView();
    }

    public List<T> getDatas() {
        return this.mDatas;
    }

    public void updateItem(int location, T item) {
        if (mDatas.isEmpty())
            return;
        mDatas.set(location, item);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        checkListNull();
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public void addItem(int location, T item) {
        checkListNull();
        mDatas.add(location, item);
        notifyDataSetChanged();
    }

    public void addItem(List<T> items) {
        checkListNull();
        if (items != null) {
            List<T> date = new ArrayList<>();
            if (mPreData != null) {
                for (T d : items) {
                    if (!mPreData.contains(d)) {
                        date.add(d);
                    }
                }
            } else {
                date = items;
            }
            mPreData = items;
            mDatas.addAll(date);
        }
        notifyDataSetChanged();
    }

    public void addItem(int position, List<T> items) {
        checkListNull();
        mDatas.addAll(position, items);
        notifyDataSetChanged();
    }

    public void removeItem(int location) {
        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }
        mDatas.remove(location);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }
        mPreData = null;
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void checkListNull() {
        if (mDatas == null) {
            mDatas = new ArrayList<T>();
        }
    }

    /**
     * 设置视图
     *
     * @param vh       ViewHolder对象
     * @param item     数据对象
     * @param position 位置
     */
    protected abstract void convert(BaseViewHolder vh, T item, int position);

    /**
     * 获取布局id
     *
     * @param position 位置
     * @param item     数据对象
     * @return
     */
    protected abstract int getLayoutId(int position, T item);
}
