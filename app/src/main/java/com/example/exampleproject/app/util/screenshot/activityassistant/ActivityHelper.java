package com.example.exampleproject.app.util.screenshot.activityassistant;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.exampleproject.app.util.screenshot.core.Constants;
import com.example.exampleproject.app.util.screenshot.helper.Callback;

public class ActivityHelper {
    private static final Class sActivityThreadClass;
    private static final Field sActivitiesField;
    private static final Field sPausedField;
    private static final Field sStoppedField;
    private static final Field sActivityField;

    private static final long sWait = 100;
    private static final int sRetriesMax = 20;
    private static int sRetriesCounter = 0;

    // initialization
    static {
        Class tmpActivityThreadClass = null;
        Field tmpActivitiesField = null;
        Field tmpPausedField = null;
        Field tmpStoppedField = null;
        Field tmpActivityField = null;
        try {
            tmpActivityThreadClass = Class.forName("android.app.ActivityThread");
            tmpActivitiesField = tmpActivityThreadClass.getDeclaredField("mActivities");
            tmpActivitiesField.setAccessible(true);
            Class activityClientRecordClass = Class.forName("android.app.ActivityThread$ActivityClientRecord");
            tmpPausedField = activityClientRecordClass.getDeclaredField("paused");
            tmpStoppedField = activityClientRecordClass.getDeclaredField("stopped");
            tmpActivityField = activityClientRecordClass.getDeclaredField("activity");
            tmpPausedField.setAccessible(true);
            tmpStoppedField.setAccessible(true);
            tmpActivityField.setAccessible(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        sActivityThreadClass = tmpActivityThreadClass;
        sActivitiesField = tmpActivitiesField;
        sPausedField = tmpPausedField;
        sStoppedField = tmpStoppedField;
        sActivityField = tmpActivityField;
    }

    public static Activity getCurrentActivity() {
        Activity activity = null;
        try {
            Object currentActivityThread = sActivityThreadClass.getMethod("currentActivityThread").invoke(null);
            Map<Object, Object> activities;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activities = (ArrayMap<Object, Object>) sActivitiesField.get(currentActivityThread);
            } else {
                activities = (HashMap<Object, Object>) sActivitiesField.get(currentActivityThread);
            }
            for (Object activityClientRecord : activities.values()) {
                boolean paused = sPausedField.getBoolean(activityClientRecord);
                boolean stopped = sStoppedField.getBoolean(activityClientRecord);
                if (!paused && !stopped) {
                    activity = (Activity) sActivityField.get(activityClientRecord);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return activity;
    }

    public static void performTaskWhenActivityIsReady(final Class<?> T, final Callback callback) {
        if (sRetriesCounter < sRetriesMax) {
            if (T.isInstance(getCurrentActivity())) {
                Log.i(Constants.LOG_TAG, String.format("ℹ %s is ready", T.getSimpleName()));
                sRetriesCounter = 0;
                callback.execute();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performTaskWhenActivityIsReady(T, callback);
                    }
                }, sWait);
                sRetriesCounter += 1;
            }
        } else {
            Log.i(Constants.LOG_TAG, String.format("☠ Timeout: can not find %s", T.getSimpleName()));
            sRetriesCounter = 0;
        }
    }

    public static void performTaskWhenLayoutStateChanges(Activity activity, final Runnable task, final long delay) {
        final View contentView = activity.findViewById(android.R.id.content);
        ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // call getViewTreeObserver() again, because previous ViewTreeObserver is not alive
                ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                }
                new Handler().postDelayed(task, delay);
            }
        });
    }

    public static void performTaskWhenLayoutStateChanges(Runnable task, long delay) {
        performTaskWhenLayoutStateChanges(getCurrentActivity(), task, delay);
    }
}
