package com.example.exampleproject.module.startup;

import android.os.Bundle;

import com.example.exampleproject.R;
import com.example.exampleproject.base.activity.BaseActivity;
import com.example.exampleproject.util.SystemUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 启动界面
 * Created by chang on 2017/3/13.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View{

    private SplashContract.Presenter mPresenter;

    @Override
    protected void getBundleData(Bundle bundle) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initData() {
        new SplashPresenter(this);
    }

    @Override
    protected void onGrantedPermission() {
        super.onGrantedPermission();
        mPresenter.timer();
    }

    @Override
    public void initWindow() {
        SystemUtil.setFullScreen(this);
    }

    @Override
    public void toLogin() {

    }

    @Override
    public RxAppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.checkAppDir();
    }
}
