package com.example.exampleproject.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by chang on 2016/8/16.
 */
public class ToastUtil {

    private static Toast toast;

    public static void showLong(Context context, String message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    public static void showShort(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, int textId) {
        show(context, textId, Toast.LENGTH_LONG);
    }

    public static void showShort(Context context, int textId) {
        show(context, textId, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void show(Context context, int textId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, textId, duration);
        } else {
            toast.setText(textId);
        }
        toast.show();
    }
}
