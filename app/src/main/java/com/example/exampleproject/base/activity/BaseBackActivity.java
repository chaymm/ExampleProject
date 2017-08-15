package com.example.exampleproject.base.activity;


import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

/**
 * 带返回功能Activity基类
 * Created by chang on 2017/2/7.
 */
public abstract class BaseBackActivity extends BaseActivity {

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void initWidget() {
        if (getToolbar() != null) {
            setSupportActionBar(getToolbar());
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    /**
     * 获取Toolbar
     *
     * @return
     */
    protected abstract Toolbar getToolbar();
}
