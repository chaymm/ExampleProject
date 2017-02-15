package com.example.exampleproject.core.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

/**
 * 公共异步线程类
 * 
 * @author chang
 * 
 */
public class BaseAsyncTask extends AsyncTask<Object, Void, Object> {

	public Context mContext;
	public ProgressDialog mDialog;
	/** 标识是否显示提示框 */
	public boolean showDialog;
	/** 线程回调接口 */
	public IAsyncTaskCallBack callback;
	/** 提示框内容 */
	public String mDialogMsg;
	/** 标识是否继续执行线程 */
	public boolean mAllowInto = true;

	public BaseAsyncTask(Context mContext, boolean showDialog, String mDialogMsg) {
		super();
		this.mContext = mContext;
		this.showDialog = showDialog;
		this.mDialogMsg = mDialogMsg;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (showDialog) {
			mDialog = ProgressDialog.show(mContext, "", mDialogMsg, true);
			mDialog.setCancelable(true);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}

			});
		}
	}

	@Override
	protected Object doInBackground(Object... values) {
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		dismissDialog();
	}

	@Override
	protected void onCancelled() {
		mAllowInto = false;
	}

	/**
	 * 设置线程执行完监听事件
	 * 
	 * @param iAsyncTaskCallBack
	 *            线程回调函数
	 */
	public void setPostExecuteListener(IAsyncTaskCallBack iAsyncTaskCallBack) {
		this.callback = iAsyncTaskCallBack;
	}

	public void dismissDialog() {
		if (showDialog) {
			mDialog.dismiss();
		}
	}

}
