package com.example.exampleproject.module.update;

import android.app.Notification;
import android.content.Context;
import android.text.TextUtils;

import com.example.exampleproject.AppConfig;
import com.example.exampleproject.R;
import com.example.exampleproject.module.update.domain.usercase.UpdateUserCase;
import com.example.exampleproject.module.update.domain.usercase.UpdateUserCaseImpl;
import com.example.exampleproject.util.IntentUtil;
import com.example.exampleproject.util.notification.NotificationEntity;
import com.example.exampleproject.util.notification.NotificationUtil;
import com.example.exampleproject.util.notification.ProgressEntity;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chang on 2017/3/13.
 */

public class UpdatePresenter implements UpdateContract.Presenter {

    private Context mContext;
    private UpdateContract.View mView;
    private UpdateUserCase mUpdateUserCase;
    private NotificationEntity notificationEntity;
    private ProgressEntity progressEntity;
    private NotificationUtil mNotificationUtil;
    private final static int REQUEST_CODE = 0X1001;

    public UpdatePresenter(UpdateContract.View view) {
        mContext = view.getDialogFragment().getActivity();
        mUpdateUserCase = new UpdateUserCaseImpl(mContext);
        mView = view;
        mView.setPresenter(this);
        mNotificationUtil = NotificationUtil.getInstance(mContext);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void getAppApk(String institutionCode, String url) {
        createNotification();
        mUpdateUserCase.getAppApk(AppConfig.getAppApkPath(), AppConfig.APP_APK_NAME, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        if(TextUtils.isEmpty(s)){
                            onError(null);
                            return;
                        }
                        int progress = Integer.parseInt(s);
                        progressEntity.setMax(100);
                        progressEntity.setProgress(progress);
                        notificationEntity.setText(progress + "%");
                        notificationEntity.setProgressEntity(progressEntity);
                        mNotificationUtil.notify(REQUEST_CODE, notificationEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mNotificationUtil.cancel(REQUEST_CODE);
                    }

                    @Override
                    public void onComplete() {
                        mNotificationUtil.cancel(REQUEST_CODE);
                        installApk();
                    }
                });
    }


    @Override
    public void cancelDownloadAppApk() {
        mUpdateUserCase.cancelDownloadAppApk();
    }

    /**
     * 安装Apk
     */
    private void installApk() {
        IntentUtil.intentInstallApk(mContext, new File(AppConfig.getAppApkName()));
    }

    /**
     * 初始化通知实体类
     */
    private void createNotification() {
        progressEntity = new ProgressEntity();
        notificationEntity = new NotificationEntity();
        notificationEntity.setDefaults(Notification.DEFAULT_LIGHTS);
        notificationEntity.setTitle(mContext.getString(R.string.dialog_update_downloading));
        notificationEntity.setSmallIcon(R.drawable.ic_update);
        mNotificationUtil.notify(REQUEST_CODE, notificationEntity);
    }


}
