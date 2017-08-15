package com.example.exampleproject.util;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by chang on 2017/2/14.
 */

public class DialogUtil {

    public static void showPermissionDialog(final Activity activity){
        new MaterialDialog.Builder(activity)
                .title("权限申请")
                .content("需要获取必要权限")
                .positiveText("设置")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        IntentUtil.intentAppDetailSetting(activity);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                })
                .show();
    }
}
