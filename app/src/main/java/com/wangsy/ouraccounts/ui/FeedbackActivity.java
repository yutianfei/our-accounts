package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.utils.NetworkUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 用户意见反馈
 * <p/>
 * Created by wangsy on 15/11/2.
 */
public class FeedbackActivity extends Activity implements View.OnClickListener {

    // 反馈内容
    private EditText etFeedbackContent;
    // 用户联系方式
    private EditText etFeedbackAddress;
    // 是否发送错误日志
    private CheckBox cbSendErrorLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        TextView tvTitle = (TextView) findViewById(R.id.id_title);
        tvTitle.setText(R.string.setting_feedback);

        initViews();
    }

    private void initViews() {
        etFeedbackContent = (EditText) findViewById(R.id.id_edit_feedback_content);
        etFeedbackAddress = (EditText) findViewById(R.id.id_edit_feedback_address);

        LinearLayout cbSendErrorLogLayout = (LinearLayout) findViewById(R.id.id_select_error_log);
        cbSendErrorLogLayout.setOnClickListener(this);
        cbSendErrorLog = (CheckBox) findViewById(R.id.id_select_error_log_checkbox);
        cbSendErrorLog.setOnClickListener(this);

        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        ImageButton btnSend = (ImageButton) findViewById(R.id.id_title_right_btn);
        btnSend.setVisibility(View.VISIBLE);
        btnSend.setOnClickListener(this);

        // 弹出软键盘
        showKeyBoard();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_title_left_btn:
                finish();
                break;
            case R.id.id_title_right_btn:
                sendFeedback();
                break;
            case R.id.id_select_error_log:
                cbSendErrorLog.setChecked(!cbSendErrorLog.isChecked());
                break;
            default:
                break;
        }
    }

    /**
     * 发送反馈信息
     */
    private void sendFeedback() {
        String content = etFeedbackContent.getText().toString();
        String address = etFeedbackAddress.getText().toString();

        // 反馈内容为空时，不允许提交
        if ("".equals(content)) {
            Toast.makeText(this, "请填写反馈内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查网络是否可用，如果不可用，取消提交
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络是否连接了？", Toast.LENGTH_SHORT).show();
            return;
        }

        // 如果提交错误日志，检查网络是否为wifi，如果不是，提示用户是否继续
        if (cbSendErrorLog.isChecked() && !NetworkUtils.isWifiConnected(this)) {
            Toast.makeText(this, "当前网络并非wifi，发送错误日志需要消耗流量，是否继续？", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO

        finish();
    }

    /**
     * 自动弹出软键盘
     * <p/>
     * 在调用时showSoftInput时，可能界面还未加载完成，etInputComment可能还为空，所以加上一个延时任务，延迟显示
     */
    private void showKeyBoard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //调用系统输入法
                InputMethodManager inputManager = (InputMethodManager) etFeedbackContent
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etFeedbackContent, 0);
            }
        }, 200);
    }
}
