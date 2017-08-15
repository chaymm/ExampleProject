package com.example.exampleproject.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.exampleproject.R;

import java.lang.reflect.Field;

/**
 * 日期选择器
 * Created by chang on 2017/8/2.
 */

public class DatePickerDialog {

    public interface OnDatePickerListener {
        void onSelectDate(int year, int month, int day);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private OnDatePickerListener mListener;
    private DatePicker mDatePicker;
    private boolean mShowYear, mShowMonth, mShowDay;
    private int mInitYear, mInitMonth, mInitDay = 1;

    public DatePickerDialog(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public DatePickerDialog needShow(boolean showYear, boolean showMonth, boolean showDay) {
        mShowYear = showYear;
        mShowMonth = showMonth;
        mShowDay = showDay;
        return this;
    }

    public DatePickerDialog setListener(OnDatePickerListener listener) {
        mListener = listener;
        return this;
    }

    public DatePickerDialog initDate(int year, int month, int day) {
        mInitYear = year;
        mInitMonth = month;
        mInitDay = day;
        return this;
    }

    public DatePickerDialog initDate(int year, int month) {
        mInitYear = year;
        mInitMonth = month;
        return this;
    }

    public DatePickerDialog show() {
        new MaterialDialog.Builder(mContext)
                .customView(showView(), false)
                .title("请选择日期")
                .positiveText(mContext.getString(android.R.string.ok))
                .negativeText(mContext.getString(android.R.string.cancel))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth() + 1;
                        int dayOfMonth = mDatePicker.getDayOfMonth();
                        if (mListener != null)
                            mListener.onSelectDate(year, month, dayOfMonth);
                    }
                })
                .show();
        return this;
    }

    private View showView() {
        View view = mInflater.inflate(R.layout.dialog_base_datepicker, null);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        if (mInitYear > 0 && mInitMonth > 0 && mInitDay > 0)
            mDatePicker.updateDate(mInitYear, mInitMonth - 1, mInitDay);
        if (!mShowYear)
            hideYear();
        if (!mShowMonth)
            hideMonth();
        if (!mShowDay)
            hideDay();
        return view;
    }

    /**
     * 隐藏年份
     */
    private void hideYear() {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int id = Resources.getSystem().getIdentifier("year", "id", "android");
                if (id != 0) {
                    View spinner = mDatePicker.findViewById(id);
                    if (spinner != null) {
                        spinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mYearSpinner".equals(datePickerField.getName()) || ("mYearPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
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
     * 隐藏月份
     */
    private void hideMonth() {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int id = Resources.getSystem().getIdentifier("month", "id", "android");
                if (id != 0) {
                    View spinner = mDatePicker.findViewById(id);
                    if (spinner != null) {
                        spinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mMonthSpinner".equals(datePickerField.getName()) || ("mMonthPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
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
     * 隐藏日
     */
    private void hideDay() {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int id = Resources.getSystem().getIdentifier("day", "id", "android");
                if (id != 0) {
                    View spinner = mDatePicker.findViewById(id);
                    if (spinner != null) {
                        spinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
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
