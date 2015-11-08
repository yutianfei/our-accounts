package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.constants.HttpParams;
import com.wangsy.ouraccounts.constants.UrlConstants;
import com.wangsy.ouraccounts.utils.NetworkUtils;
import com.wangsy.ouraccounts.utils.OkHttpClientManager;

import java.util.Map;
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
        // 反馈内容
        etFeedbackContent = (EditText) findViewById(R.id.id_edit_feedback_content);
        etFeedbackAddress = (EditText) findViewById(R.id.id_edit_feedback_address);

        // 错误日志
        LinearLayout cbSendErrorLogLayout = (LinearLayout) findViewById(R.id.id_select_error_log);
        cbSendErrorLogLayout.setOnClickListener(this);
        cbSendErrorLog = (CheckBox) findViewById(R.id.id_select_error_log_checkbox);
        cbSendErrorLog.setOnClickListener(this);
        cbSendErrorLogLayout.setVisibility(View.GONE);

        // 返回按钮
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        // 发送按钮
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
        String feedbackContent = etFeedbackContent.getText().toString();
        String feedbackAddress = etFeedbackAddress.getText().toString();

        // 反馈内容为空时，不允许提交
        if ("".equals(feedbackContent)) {
            Toast.makeText(this, R.string.tip_feedback_content_null, Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查网络是否可用，如果不可用，取消提交
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.tip_network_is_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // 提交反馈
        if (!cbSendErrorLog.isChecked()) { // 不提交错误日志
            // 提交反馈信息
            submitFeedbackContent(feedbackContent, feedbackAddress);

        } else { // 提交错误日志

            if (!NetworkUtils.isWifiConnected(this)) {
                // 不是wifi网络，提示用户是否继续
                showIsSendErrorLogDialog(feedbackContent, feedbackAddress);

            } else {
                // wifi网络，提交反馈信息，提交错误日志
                submitFeedbackContent(feedbackContent, feedbackAddress);
                sendErrorLog();
            }
        }
    }

    /**
     * 提交反馈信息
     */
    private void submitFeedbackContent(String feedbackContent, String feedbackAddress) {
        Map<String, String> params = HttpParams.feedbackParams(feedbackContent, feedbackAddress);
        OkHttpClientManager.postAsyn(UrlConstants.HTTP_USER_FEEDBACK, params, new OkHttpClientManager.ResultCallback<Integer>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(FeedbackActivity.this, R.string.tip_submit_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Integer response) {
                // 0:反馈失败，1:反馈成功
                if (response == 1) {
                    Toast.makeText(FeedbackActivity.this, R.string.tip_submit_success, Toast.LENGTH_SHORT).show();
                    // 关闭当前页面
                    finish();
                }
            }
        });
    }

    /**
     * 发送错误日志
     */
    private void sendErrorLog() {
        // TODO
//        OkHttpClientManager.getUploadDelegate().postAsyn(UrlConstants.HTTP_USER_FEEDBACK_ERROR_LOG,
//                "error_log", file, new OkHttpClientManager.ResultCallback() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        Toast.makeText(getApplicationContext(), R.string.tip_send_log_error, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(Object response) {
//                        Toast.makeText(getApplicationContext(), R.string.tip_send_log_success, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    /**
     * 是否发送错误日志
     */
    private void showIsSendErrorLogDialog(final String feedbackContent, final String feedbackAddress) {
        final Dialog dialog = new Dialog(this, R.style.style_dialog_common);
        View view = View.inflate(this, R.layout.dialog_common, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.id_dialog_title);
        tvTitle.setText(R.string.dialog_tip);
        TextView tvMessage = (TextView) view.findViewById(R.id.id_dialog_message);
        tvMessage.setText(R.string.submit_log_confirm);

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
                // 提交反馈信息，提交错误日志
                submitFeedbackContent(feedbackContent, feedbackAddress);
                sendErrorLog();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpClientManager.cancelTag(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
