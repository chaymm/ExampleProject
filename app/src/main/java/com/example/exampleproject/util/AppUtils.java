package com.example.exampleproject.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**
 * 跟App相关的辅助类
 * 
 * @author chang
 * 
 */
public class AppUtils {
	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}
	
	/**
	 * 获取应用程序包名
	 * 
	 * @param context
	 * @return 当前应用的包名
	 */
	public static String getPackageName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.packageName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取应用程序版本名称信息
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取客户端versionCode
	 * 
	 * @return
	 */
	public static int getVersionCode(Context context, String packageName) {
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					packageName, PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionCode;
		} catch (NameNotFoundException e) {
		}
		return 0;
	}
	
	/**
	 * 获取系统所有APP应用
	 * 
	 * @param context
	 */
	public static ArrayList<AppInfo> getAllApp(Context context) {
		PackageManager manager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		// 将获取到的APP的信息按名字进行排序
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		for (ResolveInfo info : apps) {
			AppInfo appInfo = new AppInfo();

			appInfo.setAppLable(info.loadLabel(manager) + "");
			appInfo.setAppIcon(info.loadIcon(manager));
			appInfo.setAppPackage(info.activityInfo.packageName);
			appInfo.setAppClass(info.activityInfo.name);
			appList.add(appInfo);
			System.out.println("info.activityInfo.packageName="
					+ info.activityInfo.packageName);
			System.out.println("info.activityInfo.name="
					+ info.activityInfo.name);
		}

		return appList;
	}

	/**
	 * 获取用户安装的APP应用
	 * 
	 * @param context
	 */
	public static ArrayList<AppInfo> getUserApp(Context context) {
		PackageManager manager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		// 将获取到的APP的信息按名字进行排序
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		for (ResolveInfo info : apps) {
			AppInfo appInfo = new AppInfo();
			ApplicationInfo ainfo = info.activityInfo.applicationInfo;
			if ((ainfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				appInfo.setAppLable(info.loadLabel(manager) + "");
				appInfo.setAppIcon(info.loadIcon(manager));
				appInfo.setAppPackage(info.activityInfo.packageName);
				appInfo.setAppClass(info.activityInfo.name);
				appList.add(appInfo);
			}
		}

		return appList;
	}

	/**
	 * 根据包名和Activity启动类查询应用信息
	 * 
	 * @param cls
	 * @param pkg
	 * @return
	 */
	public static AppInfo getAppByClsPkg(Context context, String pkg, String cls) {
		AppInfo appInfo = new AppInfo();

		PackageManager pm = context.getPackageManager();
		Drawable icon;
		CharSequence label = "";
		ComponentName comp = new ComponentName(pkg, cls);
		try {
			ActivityInfo info = pm.getActivityInfo(comp, 0);
			icon = pm.getApplicationIcon(info.applicationInfo);
			label = pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0));
		} catch (NameNotFoundException e) {
			icon = pm.getDefaultActivityIcon();
		}
		appInfo.setAppClass(cls);
		appInfo.setAppIcon(icon);
		appInfo.setAppLable(label + "");
		appInfo.setAppPackage(pkg);

		return appInfo;
	}
}

/**
 * 应用信息实体类
 * 
 * @author chang
 * 
 */
class AppInfo {
	/** 应用名称 */
	private String appLable;
	/** 应用图标 */
	private Drawable appIcon;
	/** 应用包名 */
	private String appPackage;
	/** 应用启动类 */
	private String appClass;

	public AppInfo() {
	}

	public String getAppLable() {
		return appLable;
	}

	public void setAppLable(String appLable) {
		this.appLable = appLable;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getAppClass() {
		return appClass;
	}

	public void setAppClass(String appClass) {
		this.appClass = appClass;
	}

}
