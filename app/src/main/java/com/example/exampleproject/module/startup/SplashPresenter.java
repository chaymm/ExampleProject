package com.example.exampleproject.module.startup;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

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
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(@NonNull Long aLong) throws Exception {
                        mView.toLogin();
                        return 0;
                    }
                })
                .subscribe();
    }

    @Override
    public void checkAppDir() {

    }
}



