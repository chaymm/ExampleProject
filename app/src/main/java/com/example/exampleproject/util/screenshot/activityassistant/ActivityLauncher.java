package com.example.exampleproject.util.screenshot.activityassistant;


import com.example.exampleproject.util.screenshot.core.ScreenshotsTask;
import com.example.exampleproject.util.screenshot.helper.Callback;
import com.example.exampleproject.util.screenshot.helper.ShotCallback;

public class ActivityLauncher {

    private static long sDefaultScreenshotDelay = 3000;

    private ActivityLauncher() {
    }

    private static void startActivityAndTakeScreenshot(Class<?> T, ShotCallback shotCallback, boolean hasMap, int mapFragmentId, long screenshotDelay) {
        if (!ActivityCounter.isAnyActivityRunning) {
            ActivityCounter.isAnyActivityRunning = true;
            Callback completionCallback = new Callback() {
                @Override
                public void execute() {
                    ActivityCounter.isAnyActivityRunning = false;
                }
            };
            if (hasMap) {
                ScreenshotsTask.perform(T, shotCallback, completionCallback, mapFragmentId, screenshotDelay);
            } else {
                ScreenshotsTask.perform(T, shotCallback, completionCallback, screenshotDelay);
            }
        }
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, ShotCallback shotCallback, long screenshotDelay) {
        startActivityAndTakeScreenshot(T, shotCallback, false, 0, screenshotDelay);
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, ShotCallback shotCallback) {
        startActivityAndTakeScreenshot(T, shotCallback, sDefaultScreenshotDelay);
    }

    public static void startActivityContainsMapAndTakeScreenshot(Class<?> T, ShotCallback shotCallback, int mapFragmentId, long screenshotDelay) {
        startActivityAndTakeScreenshot(T, shotCallback, true, mapFragmentId, screenshotDelay);
    }

    public static void startActivityContainsMapAndTakeScreenshot(Class<?> T, ShotCallback shotCallback, int mapFragmentId) {
        startActivityContainsMapAndTakeScreenshot(T, shotCallback, mapFragmentId, sDefaultScreenshotDelay);
    }
}
