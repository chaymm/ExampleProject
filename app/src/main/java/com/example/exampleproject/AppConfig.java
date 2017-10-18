package com.example.exampleproject;

import com.example.exampleproject.util.SDCardUtil;

/**
 * Created by chang on 2017/3/6.
 */

public class AppConfig {

    /**
     * 应用目录名
     */
    private final static String APP_NAME = "";

    /**
     * APP APK文件名称
     */
    public final static String APP_APK_NAME = "app.apk";

    /**
     * 获取应用sd卡目录路径
     *
     * @return
     */
    public static String getAppSDPath() {
        return SDCardUtil.getSDCardPath() + APP_NAME;
    }

    /**
     * 获取APP APK文件保存路径
     *
     * @return
     */
    public static String getAppApkPath() {
        return getAppSDPath() + "/update/";
    }

    /**
     * 获取APP APK文件绝对路径名称
     *
     * @return
     */
    public static String getAppApkName() {
        return getAppApkPath() + APP_APK_NAME;
    }
}
