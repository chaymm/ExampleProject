package com.example.exampleproject.base.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.example.exampleproject.base.view.FloatView;
import com.example.exampleproject.util.DensityUtil;
import com.example.exampleproject.util.ScreenUtil;
import com.trello.rxlifecycle.components.RxDialogFragment;

/**
 * DialogFragment基类
 * Created by chang on 2017/3/10.
 */

public abstract class BaseDialogFragment extends RxDialogFragment {

    protected Activity mActivity;

    /**
     * 获得全局的，防止使用getActivity()为空
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData(getArguments());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initData();
        return initDialog();
    }

    /**
     * 定义对话框 ，为自定义样式
     *
     * @param view          视图
     * @param cancelable    是否允许返回键取消
     * @param cancelOutside 是否允许点击视图取消
     * @return
     */
    protected FloatView showView(View view, boolean cancelable, boolean cancelOutside) {
        int width = 0, height = 0;
        //        if (ScreenUtil.getDeviceSize(getActivity()) < 7) {
        width = ScreenUtil.getScreenWidth(getActivity())
                - DensityUtil.dip2px(getActivity(), 40);
        height = ScreenUtil.getScreenHeight(getActivity())
                - DensityUtil.dip2px(getActivity(), 80);
        //        } else {
        //            width = getResources().getDimensionPixelSize(R.dimen.base_dialog_fragment_width);
        //            height = getResources().getDimensionPixelSize(R.dimen.base_dialog_fragment_height);
        //        }
        FloatView mFloatView = new FloatView(getActivity(), cancelable,
                cancelOutside);
        mFloatView.addContentView(view, new LinearLayout.LayoutParams(width,
                height));
        return mFloatView;
    }

    /**
     * 获取Bundle数据
     *
     * @param bundle bundle对象
     */
    protected abstract void getBundleData(Bundle bundle);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化Dialog
     *
     * @return
     */
    protected abstract Dialog initDialog();

}
