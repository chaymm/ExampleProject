package com.example.exampleproject.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * 伸缩适配器基类
 * Created by chang on 2017/4/19.
 */

public abstract class BaseExpandableListXAdapter<T> extends BaseExpandableListAdapter {

    protected Context mContext;
    private List<T> mList;

    public BaseExpandableListXAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
    }

    public List<T> getList() {
        return this.mList;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public T getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        T t = getGroup(groupPosition);
        int layoutId = getGroupLayoutId();
        final BaseViewHolder vh = BaseViewHolder.getViewHolder(convertView, parent, layoutId, groupPosition);
        convertGroup(vh, t, groupPosition);
        return vh.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        T t = getGroup(groupPosition);
        int layoutId = getChildLayoutId();
        final BaseViewHolder vh = BaseViewHolder.getViewHolder(convertView, parent, layoutId, childPosition);
        convertChild(vh, t, groupPosition, childPosition);
        return vh.getConvertView();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 组视图
     *
     * @param vh            ViewHolder对象
     * @param item          集合对象
     * @param groupPosition 组位置
     */
    protected abstract void convertGroup(BaseViewHolder vh, T item, int groupPosition);

    /**
     * 子视图
     *
     * @param vh            ViewHolder对象
     * @param item          集合对象
     * @param groupPosition 组位置
     * @param childPosition 子位置
     */
    protected abstract void convertChild(BaseViewHolder vh, T item, int groupPosition, int childPosition);

    /**
     * 获取组视图布局id
     *
     * @return
     */
    protected abstract int getGroupLayoutId();

    /**
     * 获取子视图布局id
     *
     * @return
     */
    protected abstract int getChildLayoutId();
}
