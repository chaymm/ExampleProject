package com.example.exampleproject.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import java.io.File;

/**
 * 系统工具类
 *
 * @author chang
 */
public class SystemUtil {

    /**
     * 根据/system/bin/或/system/xbin目录下是否存在su文件判断是否已ROOT
     *
     * @return true：已ROOT
     */
    public static boolean isRoot() {
        try {
            return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            return false;
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
     * @param context
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
     * @param context
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
     *
     * @param context
     * @return
     */
    public static int getMaxSystemVolumn(Context context) {
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
     *
     * @param context
     * @return
     */
    public static int getMaxMusicVolumn(Context context) {
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

    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     *
     * @param activity
     */
    public static void hideBottomUIMenu(Activity activity) {
        View v = activity.getWindow().getDecorView();
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            v.setSystemUiVisibility(uiOptions);
        }
    }

}
