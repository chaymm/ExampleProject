package com.example.exampleproject.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author chang
 *
 */
public class DateUtils {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 返回当天日期的字符串形式
	 * 
	 * @param format
	 *            形如yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateOfString(String format) {
		if (format == null || "".equals(format))
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}
	
	
	/**
	 * 根据format格式化date
	 */
	public static String formatDate(Date date,String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 返回当天日期的字符串形式,格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateOfString() {
		return dateFormat.format(new Date());
	}
	
	/**
	 * 返回日期格式，如yyyy-MM-dd,yyyyMMdd,yyyyMMddHHmmss
	 * @param date 需要格式化的日期字符串
	 * @param format 格式
	 * @return
	 */
	public static Date getDateFromString(String date,String format) {
		if (date != null && !"".equals(date)) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				return dateFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 返回日期的字符串格式形式，如yyyy-MM-dd,yyyyMMdd,yyyyMMddHHmmss
	 * @param date  需要格式化的日期
	 * @param format  格式
	 * @return
	 */
	public static String getDateToString(String date,String format) {
		if (date != null && !"".equals(date)) {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					Date d = dateFormat.parse(date);
					return dateFormat.format(d);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

	/**
	 * 
	 * @param date
	 * @return yyyy年MM月dd日 星期*
	 */
	public static String dateFormat(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			String datestr = dateFormat.format(date);
			String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" }; 
			System.out.println(date.getDay()); 
			datestr = datestr +"  "+ dayNames[date.getDay()]; 
			return datestr;
		}
		return null;
	}
	
	//返回Calendar的午夜时间(开始瞬间)
	public static  Calendar midnightOf(Calendar calendar) {
		Calendar newCalendar = (Calendar) calendar.clone();
		newCalendar.set(Calendar.HOUR_OF_DAY, 0);
		newCalendar.set(Calendar.MINUTE, 0);
		newCalendar.set(Calendar.SECOND, 0);
		newCalendar.set(Calendar.MILLISECOND, 0);
		return newCalendar;
	}
	
	
	//计算intervalMs毫秒数等于多少天，不能整除时小数点后面部分
	public static int numOfDayWithIntervalInMilliseconds(long intervalMs){
		return Math.round((intervalMs / (1000 * 86400)));
	}
}
