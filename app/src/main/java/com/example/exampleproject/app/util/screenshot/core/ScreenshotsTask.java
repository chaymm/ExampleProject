package com.example.exampleproject.app.util.screenshot.core;

import android.os.Handler;

import com.example.exampleproject.app.util.screenshot.activityassistant.ActivityHelper;
import com.example.exampleproject.app.util.screenshot.helper.Callback;
import com.example.exampleproject.app.util.screenshot.helper.KeyboardHelper;
import com.example.exampleproject.app.util.screenshot.helper.ShotCallback;


public class ScreenshotsTask {
    private static void perform(final Class<?> T, final ShotCallback shotCallback, final Callback completionCallback, final boolean hasMap, final int mapFragmentId, final long screenshotDelay) {
        new Runnable() {
            @Override
            public void run() {
                ActivityHelper.performTaskWhenActivityIsReady(T, new Callback() {
                    @Override
                    public void execute() {
                        KeyboardHelper.hideKeyboard();
                        if (hasMap) {
                            ScreenshotsCapturer.executeWithMap(ActivityHelper.getCurrentActivity(), mapFragmentId, screenshotDelay,shotCallback, completionCallback);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ScreenshotsCapturer.execute(ActivityHelper.getCurrentActivity(),shotCallback, completionCallback);
                                }
                            }, screenshotDelay);
                        }
                    }
                });
            }
        }.run();
    }

    public static void perform(Class<?> T, ShotCallback shotCallback, Callback completionCallback, long screenshotDelay) {
        perform(T, shotCallback, completionCallback, false, 0, screenshotDelay);
    }

    public static void perform(Class<?> T, ShotCallback shotCallback, Callback completionCallback, int mapFragmentId, long screenshotDelay) {
        perform(T, shotCallback, completionCallback, true, mapFragmentId, screenshotDelay);
    }
}
