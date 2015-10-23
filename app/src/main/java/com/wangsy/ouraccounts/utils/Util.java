package com.wangsy.ouraccounts.utils;

import java.text.SimpleDateFormat;

/**
 * 工具方法类
 * <p/>
 * Created by wangsy on 15/3/12.
 */
public class Util {

    /**
     * 将日期转化为字符串
     */
    public static String coverDateToString(java.util.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        return format.format(date);
    }

}
