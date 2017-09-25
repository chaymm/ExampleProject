package com.example.exampleproject.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 捕获异常信息处理类
 * 
 * @author chang
 * 
 */
public class CrashUtil implements UncaughtExceptionHandler {

	/** 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能 */
	public static final boolean DEBUG = true;
	/** 系统默认的UncaughtException处理类 */
//	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashUtil INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;
	private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private String mLogcatPath;

	/**
	 * 保证只有一个CrashHandler实例
	 */
	private CrashUtil() {
	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 */
	public static CrashUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashUtil();
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx, String logcatPath) {
		mContext = ctx;
		mLogcatPath = logcatPath;
//		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
//		if (!handleException(ex) && mDefaultHandler != null) {
//			// 如果用户没有处理则让系统默认的异常处理器来处理
//			mDefaultHandler.uncaughtException(thread, ex);
//		} else { // 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//			}
//			System.exit(0);
//		}
		Log.i("crash", "uncaughtException");
		if (!handleException(ex)) {
			System.exit(0);
		}else { // 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @return true代表处理该异常，不再向上抛异常，
	 *         false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
	 *         简单来说就是true不会弹出那个错误提示框，false就会弹出
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
//		new Thread() {  
//            @Override  
//            public void run() {  
//                Looper.prepare();  
//                Toast.makeText(mContext, "很抱歉,程序出现异常,即将返回登录.", Toast.LENGTH_LONG).show();  
//                Looper.loop();  
//            }  
//        }.start(); 
		new LogDumper().start();
		Log.i("crash", "handleException");
		return false;
	}

	// TODO 使用HTTP Post 发送错误报告到服务器 这里不再赘述
	private void postReport(File file) {
		// 在上传的时候还可以将该app的version，该手机的机型等信息一并发送的服务器，
		// Android的兼容性众所周知，所以可能错误不是每个手机都会报错，还是有针对性的去debug比较好
	}

	private class LogDumper extends Thread {

		private Process logcatProc;
		private BufferedReader mReader = null;
		private String cmds = null;
		private String mPID;
		private FileOutputStream out = null;

		public LogDumper() {
			mPID = String.valueOf(android.os.Process.myPid());
			/**
			 * 
			 * log level：*:v , *:d , *:w , *:e , *:f , *:s
			 * 
			 * Show the current mPID process level of E and W log.
			 * 
			 * */

			// cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
			// cmds = "logcat  | grep \"(" + mPID + ")\"";//show log of all
			// level
			// cmds = "logcat -s way";//Print label filtering information

			cmds = "logcat *:e *:w *:i | grep \"(" + mPID + ")\"";

		}

		@Override
		public void run() {
			Looper.prepare();
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File dir = new File(mLogcatPath);
				if (!dir.exists())
					dir.mkdirs();
				try {
					out = new FileOutputStream(new File(mLogcatPath,
							System.currentTimeMillis() + ".log"), true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					logcatProc = Runtime.getRuntime().exec(cmds);
					mReader = new BufferedReader(new InputStreamReader(
							logcatProc.getInputStream()), 1024);
					out.write(getAppInfo().getBytes());
					String line = null;
					while ((line = mReader.readLine()) != null) {
						if (line.length() == 0) {
							continue;
						}
						if (out != null && line.contains(mPID)) {
							out.write((simpleDateFormat2.format(new Date())
									+ "  " + line + "\n").getBytes());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (logcatProc != null) {
						logcatProc.destroy();
						logcatProc = null;
					}
					if (mReader != null) {
						try {
							mReader.close();
							mReader = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						out = null;
					}
				}
				Looper.loop();
			}
		}

	}

	/**
	 * 获取应用信息
	 * 
	 * @return
	 */
	private String getAppInfo() {
		PackageInfo pinfo = getPackageInfo(mContext);
		StringBuffer info = new StringBuffer();
		info.append("Version: " + pinfo.versionName + "(" + pinfo.versionCode
				+ ")\n");
		info.append("Android: " + android.os.Build.VERSION.RELEASE + "("
				+ android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL
				+ ")\n");
		return info.toString();
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	private PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}
}
