package com.example.exampleproject.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘相关辅助类
 * 
 * @author chang
 * 
 */
public class KeyBoardUtils {
	/**
	 * 强制打开软键盘
	 * 
	 * @param editText
	 *            输入框
	 * @param context
	 *            上下文
	 */
	public static void openKeyboard(EditText editText, Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param editText
	 *            输入框
	 * @param context
	 *            上下文
	 */
	public static void closeKeyboard(EditText editText, Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * 输入法在窗口上已经显示，则隐藏，反之则显示
	 * 
	 * @param context
	 *            上下文
	 */
	public static void openKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 隐藏系统默认输入法
	 * 
	 * @param activity
	 *            当前activity
	 */
	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(
				activity.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 获取输入法打开的状态
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static boolean getKeyboardState(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

}
