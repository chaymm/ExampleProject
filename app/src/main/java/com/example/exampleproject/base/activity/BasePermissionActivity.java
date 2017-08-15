package com.example.exampleproject.base.activity;

import android.support.annotation.NonNull;

import com.example.exampleproject.util.PermissionUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


/**
 * 设置权限Activity基类
 * Created by chang on 2017/2/7.
 */

public class BasePermissionActivity extends RxAppCompatActivity {
    /**
     * 权限回调Handler
     */
    private PermissionHandler mHandler;

    /**
     * 请求权限
     *
     * @param permissions 权限列表
     * @param handler     回调
     */
    protected void requestPermission(String[] permissions, PermissionHandler handler) {
        if (PermissionUtil.hasSelfPermissions(this, permissions)) {
            handler.onGranted();
        } else {
            mHandler = handler;
            //            ActivityCompat.requestPermissions(this, permissions, 001);
            handler.onRequestPermission();
        }
    }


    /**
     * 权限请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (mHandler == null)
            return;

        //        if (PermissionUtil.getTargetSdkVersion(this) < 23 && !PermissionUtil.hasSelfPermissions(this, permissions)) {
        //            mHandler.onDenied();
        //            return;
        //        }

        if (PermissionUtil.verifyPermissions(grantResults)) {
            mHandler.onGranted();
        } else {
            if (!PermissionUtil.shouldShowRequestPermissionRationale(this, permissions)) {
                mHandler.onNeverAsk();
            } else {
                mHandler.onDenied();
            }
        }
    }


    /**
     * 权限回调接口
     */
    public abstract class PermissionHandler {
        /**
         * 权限申请
         */
        public abstract void onRequestPermission();

        /**
         * 权限通过
         */
        public abstract void onGranted();

        /**
         * 权限拒绝
         */
        public abstract void onDenied();


        /**
         * 不再询问
         */
        public abstract void onNeverAsk();
    }
}
