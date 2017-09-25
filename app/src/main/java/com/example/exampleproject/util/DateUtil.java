package com.example.exampleproject.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author chang
 */
public class DateUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 返回当天日期的字符串形式
     *
     * @param format 形如yyyy-MM-dd HH:mm:ss
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
    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 返回当天日期的字符串形式,格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateOfString() {
        return dateFormat.format(new Date());
    }

    /**
     * 返回日期格式，如yyyy-MM-dd,yyyyMMdd,yyyyMMddHHmmss
     *
     * @param date   需要格式化的日期字符串
     * @param format 格式
     * @return
     */
    public static Date getDateFromString(String date, String format) {
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
     *
     * @param date   需要格式化的日期
     * @param format 格式
     * @return
     */
    public static String getDateToString(String date, String format) {
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
     * @param date
     * @return yyyy年MM月dd日 星期*
     */
    public static String dateFormat(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            String datestr = dateFormat.format(date);
            String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            System.out.println(date.getDay());
            datestr = datestr + "  " + dayNames[date.getDay()];
            return datestr;
        }
        return null;
    }

    //返回Calendar的午夜时间(开始瞬间)
    public static Calendar midnightOf(Calendar calendar) {
        Calendar newCalendar = (Calendar) calendar.clone();
        newCalendar.set(Calendar.HOUR_OF_DAY, 0);
        newCalendar.set(Calendar.MINUTE, 0);
        newCalendar.set(Calendar.SECOND, 0);
        newCalendar.set(Calendar.MILLISECOND, 0);
        return newCalendar;
    }


    //计算intervalMs毫秒数等于多少天，不能整除时小数点后面部分
    public static int numOfDayWithIntervalInMilliseconds(long intervalMs) {
        return Math.round((intervalMs / (1000 * 86400)));
    }

    /**
     * 日期转毫秒
     *
     * @param date 日期
     * @return
     */
    public static long date2MillionSeconds(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 毫秒转日期
     *
     * @param millionSeconds 毫秒
     * @return
     */
    public static Date millionSeconds2Date(long millionSeconds) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        return c.getTime();
    }

    /**
     * 计算剩余时间
     *
     * @param startDateStr 开始时间
     * @param endDateStr   结束时间
     * @return
     */
    public static String remainDateToString(String startDateStr, String endDateStr) {
        Calendar calS = Calendar.getInstance();
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        calS.setTime(startDate);
        int startY = calS.get(Calendar.YEAR);
        int startM = calS.get(Calendar.MONTH);
        int startD = calS.get(Calendar.DATE);
        int startDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

        calS.setTime(endDate);
        int endY = calS.get(Calendar.YEAR);
        int endM = calS.get(Calendar.MONTH);
        //处理2011-01-10到2011-01-10，认为服务为一天
        int endD = calS.get(Calendar.DATE) + 1;
        int endDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

        StringBuilder sBuilder = new StringBuilder();
        if (endDate.compareTo(startDate) < 0) {
            return sBuilder.append("过期").toString();
        }
        int lday = endD - startD;
        if (lday < 0) {
            endM = endM - 1;
            lday = startDayOfMonth + lday;
        }
        //处理天数问题，如：2011-01-01 到 2013-12-31  2年11个月31天     实际上就是3年
        if (lday == endDayOfMonth) {
            endM = endM + 1;
            lday = 0;
        }
        int mos = (endY - startY) * 12 + (endM - startM);
        int lyear = mos / 12;
        int lmonth = mos % 12;
        if (lyear > 0) {
            sBuilder.append(lyear + "年");
        }
        if (lmonth > 0) {
            sBuilder.append(lmonth + "个月");
        }

        //假如是小于一岁的时候，
        if (lyear == 0) {
            if (lday > 0) {
                sBuilder.append(lday - 1 + "天");
            }
        } else {
            if (lday > 0) {
                sBuilder.append(lday + "天");
            }
        }
        return sBuilder.toString();
    }

    /**
     * 计算两个日期相差了几个月
     *
     * @param startDateStr 开始时间
     * @param endDateStr   结束时间
     * @return
     */
    public static int calMonth(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        try {
            bef.setTime(sdf.parse(startDateStr));
            aft.setTime(sdf.parse(endDateStr));
            int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
            int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
            return Math.abs(month + result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 毫秒转天小时分秒
     *
     * @param mss 毫秒数
     * @return
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds ";
    }

}
