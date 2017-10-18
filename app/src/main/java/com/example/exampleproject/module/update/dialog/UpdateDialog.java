package com.example.exampleproject.module.update.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.exampleproject.R;
import com.example.exampleproject.base.fragment.BaseDialogFragment;
import com.example.exampleproject.module.update.UpdateContract;
import com.example.exampleproject.module.update.UpdatePresenter;
import com.example.exampleproject.util.ToastUtil;
import com.trello.rxlifecycle2.components.RxDialogFragment;

/**
 * 版本更新Dialog
 * Created by chang on 2017/7/4.
 */

public class UpdateDialog extends BaseDialogFragment implements UpdateContract.View {

    private UpdateContract.Presenter mPresenter;
    private String institutionCode;// 检测站id
    private String message;     // 更新内容
    private String version;     // 1.1
    private boolean require;     // true强制升级|false可选升级
    private String url;         // 客户端下载地址

    @Override
    protected void getBundleData(Bundle bundle) {
        if (bundle != null) {
            institutionCode = bundle.getString("institutionCode");
            message = bundle.getString("message");
            version = bundle.getString("version");
            require = bundle.getBoolean("require");
            url = bundle.getString("url");
        }
    }

    @Override
    protected void initData() {
        new UpdatePresenter(this);
    }

    @Override
    protected Dialog initDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dialog_title_update)
                .content(version + "\n" + message)
                .positiveText(R.string.dialog_update_positive_btn)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.getAppApk(institutionCode, url);
                    }
                });
        //不是强制更改，则给个"下次再说"的按钮
        if (!require) {
            builder.negativeText(R.string.dialog_update_negative_btn);
        }else{
            builder.cancelable(false);
        }
        return builder.show();
    }

    @Override
    public RxDialogFragment getDialogFragment() {
        return this;
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showShort(getActivity(), msg);
    }

    @Override
    public void setPresenter(UpdateContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(require)
            getActivity().finish();
    }
}
