package com.example.exampleproject.data;

import io.reactivex.Observable;

/**
 * 版本升级信息数据源接口
 * Created by chang on 2017/4/17.
 */

public interface UpdateDataSource {

    /**
     * 下载APP APK
     *
     * @param fileDir  目标文件夹
     * @param fileName 目标文件
     * @param url      下载地址
     * @return
     */
    Observable<String> getAppApk(String fileDir, String fileName, String url);

    /**
     * 取消下载APP APK
     */
    void cancelDownloadAppApk();

    /**
     * 检查版本更新
     *
     * @param institutionCode 检测站id
     * @param url             获取版本更新信息地址
     * @return
     */
    Observable<String> checkUpdate(String institutionCode, String url);
}
