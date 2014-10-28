package com.wangsy.ouraccounts.utils;

import android.content.Context;

import java.util.Calendar;

/**
 * 工具方法类
 * <p/>
 * Created by wangsy on 15/3/12.
 */
public class Util {

    public static final String DATE_FORMAT = "yyyy年MM月dd日 HH:mm";

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

}
