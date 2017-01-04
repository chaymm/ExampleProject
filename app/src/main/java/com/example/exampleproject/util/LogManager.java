package com.example.exampleproject.util;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author chang
 *
 */
public class LogManager {
	private static LogManager INSTANCE = null;
	// 锁，是否关闭Log日志输出
	public static boolean LogOFF = false;

	// 是否关闭VERBOSE输出
	public static boolean LogOFF_VERBOSE = false;

	// 是否关闭debug输出
	public static boolean LogOFF_DEBUG = false;

	/**** 5中Log日志类型 *******/
	/** 调试日志类型 */
	public static final int DEBUG = 111;

	/** 错误日志类型 */
	public static final int ERROR = 112;
	/** 信息日志类型 */
	public static final int INFO = 113;
	/** 详细信息日志类型 */
	public static final int VERBOSE = 114;
	/** 警告调试日志类型 */
	public static final int WARN = 115;

	public static LogManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LogManager();
		}
		return INSTANCE;
	}
	
	/**
	 * 开启日志
	 */
	public void openLog(){
		LogOFF = false; 
	}
	
	/**
	 * 关闭日志
	 */
	public void closeLog(){
		LogOFF = true;
	}
	
	/**
	 * 开启debug输出
	 */
	public void openDebugLog(){
		LogOFF_DEBUG = false;
	}
	
	/**
	 * 关闭debug输出
	 */
	public void closeDebugLog(){
		LogOFF_DEBUG = true;
	}
	
	/** 显示，打印日志 */
	public void logShow(String Tag, String Message, int Style) {
		if (!LogOFF) {
			switch (Style) {
			case DEBUG:
				if (!LogOFF_DEBUG) {
					Log.d(Tag, Message);
				}
				break;
			case ERROR:
				Log.e(Tag, Message);
				break;
			case INFO:
				Log.i(Tag, Message);
				break;
			case VERBOSE:
				if (!LogOFF_VERBOSE) {
					Log.v(Tag, Message);
				}
				break;
			case WARN:
				Log.w(Tag, Message);
				break;
			}
		}
	}
}
