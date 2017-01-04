package com.example.exampleproject.util;

import android.content.Context;

/**
 * http后台请求处理
 * 
 * @author chang
 * 
 */
public class HttpsClientCommand implements
		IHttpsCommand {
	private static HttpsClientUtil mHttpsClientUtil = null;
	private final static Object syncObj = new Object();
	private static HttpsClientCommand instance;
	private static Context mContext;

	public static HttpsClientCommand getHttpClientCommand(
			Context context) {
		mContext = context;
		if (null == instance) {
			synchronized (syncObj) {
				instance = new HttpsClientCommand();
				mHttpsClientUtil = HttpsClientUtil
						.getInstance(context);
			}
			return instance;
		}
		return instance;
	}

}
