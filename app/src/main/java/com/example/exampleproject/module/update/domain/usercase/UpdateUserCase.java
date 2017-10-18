package com.example.exampleproject.module.update.domain.usercase;

import io.reactivex.Observable;

/**
 * 用例接口
 * Created by chang on 2017/3/13.
 */

public interface UpdateUserCase {

    /**
     * 下载应用APK
     *
     * @param fileDir  目标目录
     * @param fileName 目标文件名
     * @param url      下载地址
     * @return
     */
    Observable<String> getAppApk(String fileDir, String fileName, String url);

    /**
     * 取消下载OBD APK
     */
    void cancelDownloadAppApk();
}
