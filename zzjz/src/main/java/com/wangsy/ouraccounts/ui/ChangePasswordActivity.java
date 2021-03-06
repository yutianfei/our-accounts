package com.wangsy.ouraccounts.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.constants.HttpParams;
import com.wangsy.ouraccounts.constants.UrlConstants;
import com.wangsy.ouraccounts.model.UserStatus;
import com.wangsy.ouraccounts.utils.NetworkUtils;
import com.wangsy.ouraccounts.utils.OkHttpClientManager;
import com.wangsy.ouraccounts.utils.Utils;

import java.util.Map;

/**
 * 修改密码
 * <p/>
 * Created by wangsy on 15/11/6.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    // 旧密码
    private EditText etOldPassword;
    // 新密码
    private EditText etNewPassword;
    // 错误提示
    private TextView tvErrorTip;

    // 用户名
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // 标题
        TextView tvTitle = (TextView) findViewById(R.id.id_title);
        tvTitle.setText(R.string.title_change_password);

        // 用户名
        SharedPreferences sp = getSharedPreferences(LoginActivity.USER_SharedPreferences, MODE_PRIVATE);
        username = sp.getString(LoginActivity.USERNAME, "");
        TextView tvUsername = (TextView) findViewById(R.id.id_username);
        tvUsername.setText(username);

        initViews();
    }

    private void initViews() {
        etOldPassword = (EditText) findViewById(R.id.id_old_password);
        etNewPassword = (EditText) findViewById(R.id.id_new_password);
        tvErrorTip = (TextView) findViewById(R.id.id_error_tip);

        // 左侧按钮：取消注册，返回
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        // 修改按钮
        Button btnRegister = (Button) findViewById(R.id.id_button_change_password);
        btnRegister.setOnClickListener(this);

        // 弹出软键盘
        Utils.showKeyBoard(etOldPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_title_left_btn:
                finish();
                break;
            case R.id.id_button_change_password:
                changePassword();
                break;
            default:
                break;
        }
    }

    /**
     * 进行注册
     */
    private void changePassword() {
        tvErrorTip.setVisibility(View.GONE);

        String oldPassword = etOldPassword.getText().toString();
        final String newPassword = etNewPassword.getText().toString();

        // 新、旧密码不能为空
        if (TextUtils.isEmpty(oldPassword)) {
            tvErrorTip.setVisibility(View.VISIBLE);
            tvErrorTip.setText(R.string.tip_change_password_old_null);
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            tvErrorTip.setVisibility(View.VISIBLE);
            tvErrorTip.setText(R.string.tip_change_password_new_null);
            return;
        }

        // 检查网络是否可用，如果不可用，取消注册
        if (!NetworkUtils.isNetworkAvailable(this)) {
            toast.setText(R.string.tip_network_is_connected);
            toast.show();
            return;
        }

        Map<String, String> params = HttpParams.changePasswordParams(username, oldPassword, newPassword);
        OkHttpClientManager.postAsyn(UrlConstants.HTTP_USER_CHANGE_PASSWORD, params,
                new OkHttpClientManager.ResultCallback<UserStatus>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        toast.setText("正在提交...");
                        toast.show();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        toast.setText(R.string.tip_change_password_error);
                        toast.show();
                        Log.i("ChangePassword", e.toString());
                    }

                    @Override
                    public void onResponse(UserStatus response) {
                        // 0:旧密码错误，1:修改成功
                        switch (response.status) {
                            case 0:
                                toast.cancel();
                                tvErrorTip.setVisibility(View.VISIBLE);
                                tvErrorTip.setText(R.string.tip_change_password_old_error);
                                break;
                            case 1:
                                tvErrorTip.setVisibility(View.GONE);
                                toast.setText(R.string.tip_change_password_success);
                                toast.show();
                                // 使用SharedPreferences保存新密码
                                SharedPreferences sp = getSharedPreferences(LoginActivity.USER_SharedPreferences, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(LoginActivity.PASSWORD, newPassword);
                                editor.apply();
                                // 关闭当前界面
                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                });
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
