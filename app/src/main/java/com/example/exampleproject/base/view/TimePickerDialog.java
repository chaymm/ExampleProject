package com.example.exampleproject.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.exampleproject.R;

import java.lang.reflect.Field;

/**
 * 时间选择器
 * Created by chang on 2017/8/2.
 */

public class TimePickerDialog {

    public interface OnTimePickerListener {
        void onSelectTime(int hour, int minute);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private OnTimePickerListener mListener;
    private TimePicker mTimePicker;
    private int mInitHour, mInitMinute;

    public TimePickerDialog(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public TimePickerDialog initTime(int hour, int minute) {
        mInitHour = hour;
        mInitMinute = minute;
        return this;
    }

    public TimePickerDialog setListener(OnTimePickerListener listener) {
        mListener = listener;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void show() {
        new MaterialDialog.Builder(mContext)
                .customView(showView(), false)
                .title("请选择时间")
                .positiveText(mContext.getString(android.R.string.ok))
                .negativeText(mContext.getString(android.R.string.cancel))
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        if (mListener != null)
                            mListener.onSelectTime(hour, minute);
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private View showView() {
        View view = mInflater.inflate(R.layout.dialog_base_timepicker, null);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
        if (mInitHour > 0)
            mTimePicker.setHour(mInitHour);
        if (mInitMinute > 0)
            mTimePicker.setMinute(mInitMinute);
        return view;
    }

    /**
     * 隐藏小时
     */
    private void hideHour() {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int id = Resources.getSystem().getIdentifier("hour", "id", "android");
                if (id != 0) {
                    View spinner = mTimePicker.findViewById(id);
                    if (spinner != null) {
                        spinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mTimePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mHourSpinner".equals(datePickerField.getName()) || ("mHourPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mTimePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏分钟
     */
    private void hideMinute() {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int id = Resources.getSystem().getIdentifier("minute", "id", "android");
                if (id != 0) {
                    View spinner = mTimePicker.findViewById(id);
                    if (spinner != null) {
                        spinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mTimePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mMinuteSpinner".equals(datePickerField.getName()) || ("mMinutePicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mTimePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
