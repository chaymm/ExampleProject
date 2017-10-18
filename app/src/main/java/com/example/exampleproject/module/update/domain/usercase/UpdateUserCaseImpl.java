package com.example.exampleproject.module.update.domain.usercase;

import android.content.Context;

import com.example.exampleproject.data.UpdateRepository;

import io.reactivex.Observable;

/**
 * Created by chang on 2017/3/13.
 */

public class UpdateUserCaseImpl implements UpdateUserCase {


    private Context mContext;
    private UpdateRepository mUpdateRepository;

    public UpdateUserCaseImpl(Context context){
        this.mContext=context;
        mUpdateRepository = UpdateRepository.getInstance(mContext);
    }

    @Override
    public Observable<String> getAppApk(String fileDir, String fileName, String url) {
        return mUpdateRepository.getAppApk(fileDir,fileName,url);
    }

    @Override
    public void cancelDownloadAppApk() {
        mUpdateRepository.cancelDownloadAppApk();
    }
}
