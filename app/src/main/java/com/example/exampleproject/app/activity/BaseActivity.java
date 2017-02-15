package com.example.exampleproject.app.activity;

import android.os.Bundle;

import com.example.exampleproject.app.BaseApplication;
import com.example.exampleproject.app.util.DialogUtil;

/**
 * Created by chang on 2017/2/7.
 */

public class BaseActivity extends BasePermissionActivity{

    private final static String[] PERMISSIONS = {""};

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);
        BaseApplication.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    protected void onGrantedPermission(){
    }

    private void checkPermission(){
        requestPermission(PERMISSIONS, new PermissionHandler() {
            @Override
            public void onRequestPermission() {
                DialogUtil.showPermissionDialog(BaseActivity.this);
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


}
