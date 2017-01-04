package com.example.exampleproject.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关辅助类
 * 
 * @author chang
 * 
 */
public class NetUtils {

	/** 未知运营商 */
	private static final int ISP_UNKNOWN = -1;
	/** 中国移动 */
	private static final int CHINA_MOBILE = 0;
	/** 中国联通 */
	private static final int CHINA_UNICOM = 1;
	/** 中国电信 */
	private static final int CHINA_NET = 2;

	/** 中国移动号码 */
	private static final String REGEX_CHINA_MOBILE = "^(1(3[4-9]|5[01789]|8[78]))\\d{8}$";
	/** 中国联通号码 */
	private static final String REGEX_CHINA_UNICOM = "^(1(3[0-2]|5[256]|8[56]))\\d{8}$";
	/** 中国电信号码 */
	private static final String REGEX_CHINA_NET = "^(133)|(153)|(18[09])\\d{8}$";

	private NetUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

	}

	/**
	 * 判断是否是手机网络连接
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isCMWAP(Context context) throws Exception {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE;
	}

	/**
	 * 判断是否是蓝牙连接
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isBlueTooth(Context context) throws Exception {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_BLUETOOTH;
	}

	/**
	 * 判断网络运营商
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static int detector(String phoneNumber) {
		if (phoneNumber.matches(REGEX_CHINA_MOBILE)) {
			return CHINA_MOBILE;
		} else if (phoneNumber.matches(REGEX_CHINA_UNICOM)) {
			return CHINA_UNICOM;
		} else if (phoneNumber.matches(REGEX_CHINA_NET)) {
			return CHINA_NET;
		}
		return ISP_UNKNOWN;
	}

}
