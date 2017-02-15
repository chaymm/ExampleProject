package com.example.exampleproject.app.util.screenshot.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.example.exampleproject.app.util.screenshot.activityassistant.ActivityHelper;
import com.example.exampleproject.app.util.screenshot.helper.Callback;
import com.example.exampleproject.app.util.screenshot.helper.ShotCallback;

public class ScreenshotsCapturer {
    private ScreenshotsCapturer() {
        throw new AssertionError();
    }

    private static Bitmap captureScreenshot(Activity activity) {
        View rootView = activity.findViewById(android.R.id.content).getRootView();
        int width = rootView.getWidth();
        int height = rootView.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        rootView.draw(canvas);
        return bitmap;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static void createDirIfNotExist(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static File saveToFile(Bitmap bitmap) {
        String filename  = (String) DateFormat.format("yyyyMMdd-hhmmss", new Date().getTime());
        File screenshotDir = new File(Constants.SHOT_DIR);
        createDirIfNotExist(screenshotDir);
        File screenshot = new File(screenshotDir.getPath() + File.separator + filename + ".png");
        try {
            screenshot.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(screenshot);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return screenshot;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int[] getViewLocationOnlyIfRendered(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    private static int[] getViewLocationOnlyIfRendered(int viewId) {
        Activity activity = ActivityHelper.getCurrentActivity();
        View view;
        if (activity != null && (view = activity.findViewById(viewId)) != null) {
            return getViewLocationOnlyIfRendered(view);
        } else {
            return null;
        }
    }

    private static void handleBitmap(final Activity activity, final int mapFragmentId, final long screenshotDelay,final Bitmap bitmap, final ShotCallback shotCallback, final Callback callback){
        final Callback drawScreenshot = new Callback() {
            @Override
            public void execute() {
                View rootView = activity.findViewById(android.R.id.content).getRootView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap background = rootView.getDrawingCache();
                int width = background.getWidth();
                int height = background.getHeight();
                Bitmap bgBitmap = Bitmap.createBitmap(width,height , background.getConfig());
                Canvas canvas = new Canvas(bgBitmap);

                //地图视图
                int[] mapLocation = getViewLocationOnlyIfRendered(mapFragmentId);
                canvas.drawBitmap(bitmap, mapLocation[0], mapLocation[1], null);

                //主视图
//                View toolView = activity.findViewById(R.id.toolbar);
//                int toolViewHeight = toolView.getHeight();
//                Rect rect = new Rect(0, height-toolViewHeight, width, height);
//                Rect rect2 = new Rect(0, height-toolViewHeight, width, height);
//                Paint vPaint = new Paint();
//                vPaint .setStyle(Paint.Style.STROKE);
//                vPaint .setAlpha(240);
//                canvas.drawBitmap(background, rect, rect2, vPaint);

                //popupWindow视图
//                PopupWindow popupWindow = ChoiceBoxUtils.getPopupWindow();
//                Bitmap popupBitmap = null;
//                if(popupWindow != null && popupWindow.isShowing()){
//                    View contentView = popupWindow.getContentView();
//                    contentView.setDrawingCacheEnabled(true);
//                    popupBitmap = Bitmap.createBitmap(contentView.getDrawingCache());
//                    canvas.drawBitmap(popupBitmap,(width/2)-(contentView.getWidth()/2),15,null);
//                }

                File file = saveToFile(bgBitmap);
                if(file != null){
                    shotCallback.success(file);
                    Log.i(Constants.LOG_TAG, "♬ Screenshot is taken (including Map)");
                }else{
                    shotCallback.fail();
                    Log.i(Constants.LOG_TAG, "♬ Screenshot fail");
                }

                callback.execute();
            }
        };
        if (screenshotDelay > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawScreenshot.execute();
                }
            }, screenshotDelay);
        } else {
            drawScreenshot.execute();
        }
    }

    public static void execute(Activity activity,ShotCallback shotCallback, Callback callback) {
        File file = saveToFile(captureScreenshot(activity));
        if(file != null){
            shotCallback.success(file);
            Log.i(Constants.LOG_TAG, "♬ Screenshot is taken");
        }else{
            shotCallback.fail();
            Log.i(Constants.LOG_TAG, "♬ Screenshot fail");
        }
        callback.execute();
    }

    public static void executeWithMap(final Activity activity, final int mapFragmentId, final long screenshotDelay, final ShotCallback shotCallback, final Callback callback) {
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(mapFragmentId);
//            if (fragment instanceof GaodeMapFragment) {//高德地图
//                AMap map = ((GaodeMapFragment) fragment).getMap();
//                // map is null when Play services is missing
//                if (map != null) {
//                    map.getMapScreenShot(new AMap.OnMapScreenShotListener() {
//                        @Override
//                        public void onMapScreenShot(Bitmap bitmap) {
//                            handleBitmap(activity,mapFragmentId,screenshotDelay,bitmap,shotCallback,callback);
//                        }
//
//                        @Override
//                        public void onMapScreenShot(Bitmap bitmap, int i) {
//
//                        }
//                    });
//                } else {
//                    callback.execute();
//                    shotCallback.fail();
//                    Log.i(Constants.LOG_TAG, "♬ Screenshot fail");
//                }
//            }
//
//            if (fragment instanceof BaiduMapFragment) {//百度地图
//                BaiduMap map = ((BaiduMapFragment) fragment).getMap();
//                // map is null when Play services is missing
//                if (map != null) {
//                    map.snapshot(new BaiduMap.SnapshotReadyCallback() {
//                        @Override
//                        public void onSnapshotReady(Bitmap bitmap) {
//                            handleBitmap(activity,mapFragmentId,screenshotDelay,bitmap,shotCallback,callback);
//                        }
//                    });
//                } else {
//                    callback.execute();
//                    shotCallback.fail();
//                    Log.i(Constants.LOG_TAG, "♬ Screenshot fail");
//                }
//            }

        }
    }


}
