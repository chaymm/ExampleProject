package com.example.exampleproject.app.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;

/**
 * 系统工具类
 * 
 * @author chang
 * 
 */
public class SystemUtils {

	/**
	 * WIFI网络开关
	 * 
	 */
	public static void toggleWiFi(Context context, boolean enabled) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wm.setWifiEnabled(enabled);
	}

	/**
	 * 移动网络开关
	 */
	public static void toggleMobileData(Context context, boolean enabled) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<?> conMgrClass = null; // ConnectivityManager类
		Field iConMgrField = null; // ConnectivityManager类中的字段
		Object iConMgr = null; // IConnectivityManager类的引用
		Class<?> iConMgrClass = null; // IConnectivityManager类
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * GPS开关 当前若关则打开 当前若开则关闭
	 */
	public static void toggleGPS(Context context) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调节系统音量
	 * 
	 * @param context
	 */
	public static void holdSystemAudio(Context context) {
		AudioManager audiomanage = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 获取系统最大音量
		// int maxVolume =
		// audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 获取当前音量
		// int currentVolume =
		// audiomanage.getStreamVolume(AudioManager.STREAM_RING);
		// 设置音量
		// audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM,
		// currentVolume, AudioManager.FLAG_PLAY_SOUND);

		// 调节音量
		// ADJUST_RAISE 增大音量，与音量键功能相同
		// ADJUST_LOWER 降低音量
		audiomanage.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

	}

	/**
	 * 设置亮度（每30递增）
	 * 
	 * @param resolver
	 * @param brightness
	 */
	public static void setBrightness(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Settings.System
				.getUriFor("screen_brightness");
		int nowScreenBri = getScreenBrightness(context);
		nowScreenBri = nowScreenBri <= 225 ? nowScreenBri + 30 : 30;
		System.out.println("nowScreenBri==" + nowScreenBri);
		Settings.System.putInt(resolver, "screen_brightness",
				nowScreenBri);
		resolver.notifyChange(uri, null);
	}

	/**
	 * 设置当前屏幕亮度值 0--255
	 * 
	 * @param context
	 * @param paramInt
	 */
	public static void saveScreenBrightness(Context context, int paramInt) {
		if (paramInt < 0) {
			paramInt = 0;
		}
		if (paramInt > 255) {
			paramInt = 255;
		}
		try {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 获取屏幕的亮度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenBrightness(Context context) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = context.getContentResolver();
		try {
			nowBrightnessValue = Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	/**
	 * 是否亮度自动调节模式
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isAutoBrightness(Activity activity) {
		boolean auto = false;
		try {
			auto = Settings.System.getInt(activity.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (Settings.SettingNotFoundException e) {
			e.printStackTrace();
		}
		return auto;
	}

	/**
	 * 开始自动调节
	 * 
	 * @param activity
	 */
	public static void startAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}

	/**
	 * 停止自动调节
	 * 
	 * @param activity
	 */
	public static void stopAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}
	
	/**
     * 获得当前系统音量值
     *
     * @param context
     * @return
     */
    public static int getCurrentSystemVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        return volume;
    }

    /**
     * 设置当前系统音量值
     */
    public static void saveSystemVolume(Context context, int paramInt) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        if (paramInt < 0) {
            paramInt = 0;
        }
        if (paramInt > max) {
            paramInt = max;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, paramInt, 0);
    }
    
    /**
     * 获取系统最大音量
     * @param context
     * @return
     */
    public static int getMaxSystemVolumn(Context context){
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        return volume;
    }

	/**
	 * 获得当前音乐音量值
	 * 
	 * @param context
	 * @return
	 */
	public static int getCurrentMusicVolume(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		return volume;
	}

	/**
	 * 设置当前音乐音量值
	 */
	public static void saveMusicVolume(Context context, int paramInt) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if (paramInt < 0) {
			paramInt = 0;
		}
		if (paramInt > max) {
			paramInt = max;
		}
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, paramInt, 0);
	}

	/**
	 * 设置音乐静音
	 * 
	 * @param context
	 */
	public static void muteMusic(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
	}
	
	/**
     * 获取音乐最大音量
     * @param context
     * @return
     */
    public static int getMaxMusicVolumn(Context context){
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return volume;
    }
    
    /**
     * 设置铃声临时静音模式
     *
     * @param context
     */
    public static void setSilentRinger(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    /**
     * 设置铃声正常模式
     *
     * @param context
     */
    public static void setNormalRinger(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

}
