package com.example.exampleproject.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.exampleproject.R;
/**
 * 日期时间选择控件 
 * @author 
 */
public class DateTimePickerDialog implements  OnDateChangedListener,OnTimeChangedListener{
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Context mContext;
	
	/**
	 * 日期时间弹出选择框构
	 * @param activity：调用的父activity
	 */
	public DateTimePickerDialog(Context context)
	{
		this.mContext = context;
	}
	
	public void init(DatePicker datePicker,TimePicker timePicker)
	{
		Calendar calendar= Calendar.getInstance();
		initDateTime=calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+
				calendar.get(Calendar.DAY_OF_MONTH)+" "+
				calendar.get(Calendar.HOUR_OF_DAY)+":"+
				calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}
	
	/**
	 * 弹出日期时间选择框
	 * @param dateTimeTextEdite 需要设置的日期时间文本编辑框
	 * @param type: 0为日期时间类型:yyyy-MM-dd HH:mm:ss
	 * 						  1为日期类型:yyyy-MM-dd
	 * 						  2为时间类型:HH:mm:ss
	 * @return
	 */
	public AlertDialog dateTimePicKDialog(final TextView dateTimeTextEdite, int type)
	{
		Calendar c = Calendar.getInstance();
		switch (type) {
		case 1:
			new DatePickerDialog(mContext,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
								int dayOfMonth) {
							Calendar calendar = Calendar.getInstance();
							calendar.set(datePicker.getYear(), datePicker.getMonth(),
									datePicker.getDayOfMonth());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							dateTime=sdf.format(calendar.getTime());
							dateTimeTextEdite.setText(dateTime);
						}
					},
					c.get(Calendar.YEAR),
					c.get(Calendar.MONTH),
					c.get(Calendar.DATE)).show();
			return null;
		case 2:
			new TimePickerDialog(mContext, 
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
							Calendar calendar = Calendar.getInstance();
							calendar.set(Calendar.YEAR, Calendar.MONTH,
									Calendar.DAY_OF_MONTH, timePicker.getCurrentHour(),
									timePicker.getCurrentMinute());
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
							dateTime=sdf.format(calendar.getTime());
							dateTimeTextEdite.setText(dateTime);
						}
					}, 
					c.get(Calendar.HOUR_OF_DAY), 
					c.get(Calendar.MINUTE), 
					true).show();
			return null;
		default:
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout dateTimeLayout  = (LinearLayout) inflater.inflate(R.layout.datetime, null);
			datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
			timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
			init(datePicker,timePicker);
			timePicker.setIs24HourView(true);
			timePicker.setOnTimeChangedListener(this);
					
			ad = new AlertDialog.Builder(mContext).setIcon(R.drawable.datetimeicon).setTitle(initDateTime).setView(dateTimeLayout).setPositiveButton("设置",
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int whichButton)
								{
									//手动输入日期或时间，先将DatePicker和TimePicker的焦点清除
									datePicker.clearFocus();
									timePicker.clearFocus();
									dateTimeTextEdite.setText(dateTime);
								}
							}).setNegativeButton("取消",
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int whichButton)
								{
//									dateTimeTextEdite.setText("");
									ad.dismiss();
								}
							}).show();
			
			onDateChanged(null, 0, 0, 0);
			return ad;
		}
	}
	
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
	{
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth)
	{
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		dateTime=new StringBuffer().append(sdf.format(calendar.getTime())).append(":00").toString();
		dateTime=sdf.format(calendar.getTime());
		ad.setTitle(dateTime);
	}
	
}
