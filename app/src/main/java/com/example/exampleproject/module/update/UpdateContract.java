package com.example.exampleproject.module.update;


import com.example.exampleproject.base.BasePresenter;
import com.example.exampleproject.base.BaseView;
import com.trello.rxlifecycle2.components.RxDialogFragment;

/**
 * 协议接口
 * Created by chang on 2017/3/13.
 */

public interface UpdateContract {

    /**
     * 视图基类
     */
    interface View extends BaseView<Presenter> {
        /**
         * 获取对话框视图对象
         *
         * @return
         */
        RxDialogFragment getDialogFragment();

        /**
         * 显示信息
         *
         * @param msg
         */
        void showToast(String msg);
    }

    /**
     * 交互基类
     */
    interface Presenter extends BasePresenter {

        /**
         * 下载应用APK
         * @param institutionCode 检测站id
         * @param url      下载地址
         */
        void getAppApk(String institutionCode, String url);

        /**
         * 取消下载OBD APK
         */
        void cancelDownloadAppApk();
    }

}
