package com.wangsy.ouraccounts.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.callback.CommonDialogEvent;
import com.wangsy.ouraccounts.constants.HttpParams;
import com.wangsy.ouraccounts.constants.UrlConstants;
import com.wangsy.ouraccounts.model.UserStatus;
import com.wangsy.ouraccounts.utils.NetworkUtils;
import com.wangsy.ouraccounts.utils.OkHttpClientManager;
import com.wangsy.ouraccounts.utils.Utils;

import java.util.Map;

/**
 * 用户意见反馈
 * <p/>
 * Created by wangsy on 15/11/2.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

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
        Utils.showKeyBoard(etFeedbackContent);
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
            toast.setText(R.string.tip_feedback_content_null);
            toast.show();
            return;
        }

        // 检查网络是否可用，如果不可用，取消提交
        if (!NetworkUtils.isNetworkAvailable(this)) {
            toast.setText(R.string.tip_network_is_connected);
            toast.show();
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
        OkHttpClientManager.postAsyn(UrlConstants.HTTP_USER_FEEDBACK, params, new OkHttpClientManager.ResultCallback<UserStatus>() {
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                toast.setText("正在提交...");
                toast.show();
            }

            @Override
            public void onError(Request request, Exception e) {
                toast.setText(R.string.tip_submit_error);
                toast.show();
                Log.i("FeedbackContent", e.toString());
            }

            @Override
            public void onResponse(UserStatus response) {
                // 0:反馈失败，1:反馈成功
                if (response.status == 1) {
                    toast.setText(R.string.tip_submit_success);
                    toast.show();
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
//                        toast.setText(R.string.tip_send_log_error);
//                        toast.show();
//                        Log.i("FeedbackErrorLog", e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Object response) {
//                        toast.setText(R.string.tip_send_log_success);
//                        toast.show();
//                    }
//                });
    }

    /**
     * 是否发送错误日志
     */
    private void showIsSendErrorLogDialog(final String feedbackContent, final String feedbackAddress) {
        String message = getString(R.string.submit_log_confirm);
        Utils.getCommonDialog(this, message, new CommonDialogEvent() {
            @Override
            public void onButtonOkClick() {
                // 提交反馈信息，提交错误日志
                submitFeedbackContent(feedbackContent, feedbackAddress);
                sendErrorLog();
            }
        }).show();
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
