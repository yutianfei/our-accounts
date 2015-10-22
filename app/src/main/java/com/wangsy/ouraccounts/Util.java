package com.wangsy.ouraccounts;

import android.content.ContentValues;

import com.wangsy.ouraccounts.model.AccountModel;

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

    /**
     * 根据Account的信息创建ContentValues
     */
    public static ContentValues createContentValues(AccountModel account) {
        ContentValues values = new ContentValues();

        return values;
    }
}
