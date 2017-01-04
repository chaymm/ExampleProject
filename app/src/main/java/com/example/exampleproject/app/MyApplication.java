package com.example.exampleproject.app;

import android.app.Application;

import com.loopj.android.http.PersistentCookieStore;
import com.netease.nis.bugrpt.CrashHandler;

import org.apache.http.client.CookieStore;

public class MyApplication extends Application {

	/** 当前会话后的cokie信息 */
	private CookieStore uCookie = null;
	/** 上下文对象 */
	private String sessionId = null;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.init(getApplicationContext());
	}

	public CookieStore getuCookie() {
		return uCookie;
	}

	public void setuCookie(CookieStore uCookie) {
		this.uCookie = uCookie;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public PersistentCookieStore getPersistentCookieStore() {
		return new PersistentCookieStore(getApplicationContext());
	}

}
