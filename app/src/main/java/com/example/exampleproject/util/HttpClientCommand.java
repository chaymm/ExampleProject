package com.example.exampleproject.util;

import android.content.Context;

/**
 * http后台请求处理
 * 
 * @author chang
 * 
 */
public class HttpClientCommand implements IHttpCommand {
	private static HttpClientUtil httpClientUtil = null;
	private final static Object syncObj = new Object();
	private static HttpClientCommand instance;
	private static Context mContext;

	public static HttpClientCommand getHttpClientCommand(Context context) {
		mContext = context;
		if (null == instance) {
			synchronized (syncObj) {
				instance = new HttpClientCommand();
				httpClientUtil = HttpClientUtil.getInstance(context);
			}
			return instance;
		}
		return instance;
	}


}
