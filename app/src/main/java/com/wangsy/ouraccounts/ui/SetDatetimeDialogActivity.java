package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 设置时间
 * <p/>
 * Created by wangsy on 15/10/26.
 */
public class SetDatetimeDialogActivity extends Activity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    public static final String EXTRA_DATETIME = "extra_datetime";

    private DatePicker datePicker;
    private TimePicker timePicker;

    private String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_datetime);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 初始化确定、取消按钮及事件
        initButtons();

        // 初始化时间选择组件
        initDateTimePickers();
    }

    private void initDateTimePickers() {
        Calendar calendar = null;
        dateTime = getIntent().getStringExtra(EXTRA_DATETIME);

        if (dateTime == null || "".equals(dateTime)) {
            calendar = Calendar.getInstance();
        } else {
            calendar = Util.getCalendarByString(dateTime);
        }

        datePicker = (DatePicker) findViewById(R.id.id_date_picker);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);

        timePicker = (TimePicker) findViewById(R.id.id_time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    private void initButtons() {
        Button btnOk = (Button) findViewById(R.id.id_button_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATETIME, dateTime);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.id_button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null, 0, 0, 0);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());

        SimpleDateFormat sdf = new SimpleDateFormat(Util.DATE_FORMAT);
        dateTime = sdf.format(calendar.getTime());
    }
}