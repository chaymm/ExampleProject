package com.example.exampleproject.listener;

/**
 * 上传文件监听器
 * 
 * @author Administrator
 * 
 */
public interface OnFileUploadListener {

	/**
	 * 获取上传进度
	 * 
	 * @param progress
	 *            进度值
	 */
	public abstract void onProgress(int progress);
}
