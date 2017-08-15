package com.example.exampleproject.module.startup;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by chang on 2017/3/13.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private Context mContext;
    private SplashContract.View mView;

    public SplashPresenter(SplashContract.View view) {
        mContext = view.getActivity();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void timer() {
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        mView.toLogin();
                        return null;
                    }
                })
                .subscribe();
    }

    @Override
    public void checkAppDir() {

    }
}



