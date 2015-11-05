package com.wangsy.ouraccounts.utils;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 工具方法类
 * <p/>
 * Created by wangsy on 15/3/12.
 */
public class Utils {

    public static final String PACKAGE_NAME = "com.wangsy.ouraccounts";
    public static final String IMAGE_FOLDER = "mipmap";

    public static final String DATE_FORMAT_DAY = "yyyy年MM月dd日";
    public static final String DATE_FORMAT = "yyyy年MM月dd日 HH:mm";

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }

    public static String dateFormatWithDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DAY, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 将字符串类型的日期时间转换为Calendar类型
     */
    public static Calendar getCalendarByString(String dateTime) {
        Calendar calendar = Calendar.getInstance();

        String date = splitString(dateTime, "日", "index", "front"); // 日期
        String time = splitString(dateTime, "日", "index", "back"); // 时间

        String yearStr = splitString(date, "年", "index", "front"); // 年份
        String monthAndDay = splitString(date, "年", "index", "back"); // 月日

        String monthStr = splitString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = splitString(monthAndDay, "月", "index", "back"); // 日

        String hourStr = splitString(time, ":", "index", "front"); // 时
        String minuteStr = splitString(time, ":", "index", "back"); // 分

        int currentYear = Integer.valueOf(yearStr.trim());
        int currentMonth = Integer.valueOf(monthStr.trim()) - 1;
        int currentDay = Integer.valueOf(dayStr.trim());
        int currentHour = Integer.valueOf(hourStr.trim());
        int currentMinute = Integer.valueOf(minuteStr.trim());

        calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMinute);
        return calendar;
    }

    /**
     * 截取子串
     */
    public static String splitString(String srcStr, String pattern, String indexOrLast, String frontOrBack) {
        String result = "";
        int location = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            location = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            location = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (location != -1)
                result = srcStr.substring(0, location); // 截取子串
        } else {
            if (location != -1)
                result = srcStr.substring(location + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 根据图片名称获得图片id
     */
    public static int getImageResourceId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, IMAGE_FOLDER, PACKAGE_NAME);
    }

    /**
     * 获取时间字符串
     */
    public static String[] getDatetimeStringWithMonths(int months) {
        String[] result = new String[2];
        Calendar calendar = Calendar.getInstance();

        // 结束时间
        result[1] = dateFormatWithDay(calendar.getTime()) + " 23:59";

        // 根据传入的参数设置日历
        calendar.add(Calendar.MONTH, months);
        // 开始时间
        result[0] = dateFormatWithDay(calendar.getTime()) + " 00:00";

        return result;
    }

    /**
     * float转百分比，保留一位小数
     */
    public static String convertFloatToPercent(float number) {
        float convertNumber = number * 100;
        DecimalFormat format = new DecimalFormat("##0.0");
        return format.format(convertNumber) + "%";
    }
}
