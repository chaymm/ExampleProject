package com.example.exampleproject.base.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.exampleproject.R;
import com.example.exampleproject.base.BaseApplication;
import com.example.exampleproject.util.IntentUtil;
import com.example.exampleproject.util.SystemBarTintManager;

/**
 * Created by chang on 2017/2/7.
 */

public abstract class BaseActivity extends BasePermissionActivity {

    protected Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(getLayoutId());
        BaseApplication.addActivity(this);

        getBundleData(getIntent().getExtras());

        initWidget();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    /**
     * 查找组件
     *
     * @param viewId：View的ID
     * @return View
     */
    @SuppressWarnings("unchecked")
    protected <view extends View> view getViewById(int viewId) {
        return (view) findViewById(viewId);
    }

    /**
     * 填充视图
     *
     * @param resId
     * @return
     */
    protected View inflateView(int resId) {
        return getLayoutInflater().inflate(resId, null);
    }

    /**
     * 添加Fragment
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    /**
     * 添加Fragment(返回处理)
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected void addFragmentToBackStack(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * 替换Fragment
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            mFragment = fragment;
            transaction.commit();
        }
    }

    /**
     * 检查隐私权限
     */
    private void checkPermission() {
        requestPermission(BaseApplication.PERMISSIONS, new PermissionHandler() {
            @Override
            public void onRequestPermission() {
                showPermissionDialog();
            }

            @Override
            public void onGranted() {
                onGrantedPermission();
            }

            @Override
            public void onDenied() {

            }

            @Override
            public void onNeverAsk() {

            }
        });
    }

    /**
     * 初始化窗口管理
     */
    public void initWindow() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }
    }

    /**
     * 分发处理点击事件，隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示获取权限提示框
     */
    private void showPermissionDialog() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.dialog_title_permission))
                .content(getString(R.string.dialog_content_permission))
                .positiveText(getString(R.string.btn_setting))
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        IntentUtil.intentAppDetailSetting(BaseActivity.this);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    /**
     * 允许权限操作后
     */
    protected void onGrantedPermission() {
    }

    /**
     * 获取Bundle数据
     *
     * @param bundle bundle对象
     */
    protected abstract void getBundleData(Bundle bundle);

    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initWidget();

    /**
     * 初始化数据
     */
    protected abstract void initData();

}
