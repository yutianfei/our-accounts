package com.wangsy.ouraccounts.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.callback.CommonDialogEvent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
     * px转dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
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

    /**
     * 通用提示对话框
     */

    public static Dialog getCommonDialog(Context context, final CommonDialogEvent event) {
        return getCommonDialog(context, null, null, event);
    }

    public static Dialog getCommonDialog(Context context, String message, final CommonDialogEvent event) {
        return getCommonDialog(context, null, message, event);
    }

    public static Dialog getCommonDialog(Context context, String title, String message, final CommonDialogEvent event) {
        final Dialog dialog = new Dialog(context, R.style.style_dialog_common);
        View view = View.inflate(context, R.layout.dialog_common, null);

        if (!TextUtils.isEmpty(title)) {
            TextView tvTitle = (TextView) view.findViewById(R.id.id_dialog_title);
            tvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView tvMessage = (TextView) view.findViewById(R.id.id_dialog_message);
            tvMessage.setText(message);
        }

        Button btnCancel = (Button) view.findViewById(R.id.id_button_cancel);
        Button btnOk = (Button) view.findViewById(R.id.id_button_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.onButtonOkClick();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    /**
     * 自动弹出软键盘
     * <p/>
     * 在调用时showSoftInput时，可能界面还未加载完成，etInputComment可能还为空，所以加上一个延时任务，延迟显示
     */
    public static void showKeyBoard(final EditText et) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //调用系统输入法
                InputMethodManager inputManager = (InputMethodManager) et.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et, 0);
            }
        }, 200);
    }
}
