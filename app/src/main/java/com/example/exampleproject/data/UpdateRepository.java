package com.example.exampleproject.data;

import android.content.Context;

import com.example.exampleproject.data.remote.UpdateRemoteDataSource;

import io.reactivex.Observable;

/**
 * 版本升级信息仓库
 * Created by chang on 2017/4/17.
 */

public class UpdateRepository implements UpdateDataSource {

    private static UpdateRepository INSTANCE = null;
    private UpdateRemoteDataSource mUpdateRemoteDataSource;

    private UpdateRepository(Context context) {
        mUpdateRemoteDataSource = UpdateRemoteDataSource.getInstance(context);
    }

    public static UpdateRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UpdateRepository(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<String> getAppApk(String fileDir, String fileName,String url) {
        return mUpdateRemoteDataSource.getAppApk(fileDir,fileName,url);
    }

    @Override
    public void cancelDownloadAppApk() {
        mUpdateRemoteDataSource.cancelAll();
    }

    @Override
    public Observable<String> checkUpdate(String institutionCode,String url) {
        return mUpdateRemoteDataSource.checkUpdate(institutionCode,url);
    }
}
