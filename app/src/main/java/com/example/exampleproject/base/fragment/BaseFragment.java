package com.example.exampleproject.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Fragment基类
 * Created by chang on 2017/2/23.
 */

public abstract class BaseFragment extends RxFragment {

    protected Activity mActivity;
    protected LayoutInflater mInflater;
    protected View mView;

    /**
     * 获得全局的，防止使用getActivity()为空
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mInflater = inflater;
        mView = mInflater.inflate(getLayoutId(),container,false);

        initWidget(mView);
        initData();

        return mView;
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    protected <T extends View> T getViewById(int viewId) {
        return (T) mView.findViewById(viewId);
    }

    protected abstract void getBundleData(Bundle bundle);

    /**
     * 初始化布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 该抽象方法就是 初始化view
     *
     * @param root
     */
    protected abstract void initWidget(View root);

    protected abstract void initData();
}
