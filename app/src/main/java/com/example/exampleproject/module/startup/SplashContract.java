package com.example.exampleproject.module.startup;


import com.example.exampleproject.base.BasePresenter;
import com.example.exampleproject.base.BaseView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * 协议接口
 * Created by chang on 2017/3/13.
 */

public interface SplashContract {

    /**
     * 视图基类
     */
    interface View extends BaseView<Presenter> {

        /**
         * 跳转登录
         */
        void toLogin();

        /**
         * 获取当前Activity
         *
         * @return
         */
        RxAppCompatActivity getActivity();

    }

    /**
     * 交互基类
     */
    interface Presenter extends BasePresenter {

        /**
         * 定时操作
         */
        void timer();

        /**
         * 查询应用目录
         */
        void checkAppDir();
    }

}
