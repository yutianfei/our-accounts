package com.wangsy.ouraccounts.ui;

import android.content.Intent;
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

import java.util.Map;

/**
 * 注册
 * <p/>
 * Created by wangsy on 15/11/6.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    // 用户名
    private EditText etUsername;
    // 密码框
    private EditText etPassword;
    // 错误提示
    private TextView tvErrorTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tvTitle = (TextView) findViewById(R.id.id_title);
        tvTitle.setText(R.string.title_register);

        initViews();
    }

    private void initViews() {
        etUsername = (EditText) findViewById(R.id.id_register_username);
        etPassword = (EditText) findViewById(R.id.id_register_password);
        tvErrorTip = (TextView) findViewById(R.id.id_error_tip);

        // 左侧按钮：取消注册，返回
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        // 注册按钮
        Button btnRegister = (Button) findViewById(R.id.id_button_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_title_left_btn:
                finish();
                break;
            case R.id.id_button_register:
                register();
                break;
            default:
                break;
        }
    }

    /**
     * 进行注册
     */
    private void register() {
        tvErrorTip.setVisibility(View.GONE);

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        // 用户名、密码不能为空
        if (TextUtils.isEmpty(username)) {
            tvErrorTip.setVisibility(View.VISIBLE);
            tvErrorTip.setText(R.string.tip_username_null);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            tvErrorTip.setVisibility(View.VISIBLE);
            tvErrorTip.setText(R.string.tip_password_null);
            return;
        }

        // 检查网络是否可用，如果不可用，取消注册
        if (!NetworkUtils.isNetworkAvailable(this)) {
            toast.setText(R.string.tip_network_is_connected);
            toast.show();
            return;
        }

        Map<String, String> params = HttpParams.registerParams(username, password);
        OkHttpClientManager.postAsyn(UrlConstants.HTTP_USER_REGISTER, params,
                new OkHttpClientManager.ResultCallback<UserStatus>() {

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        toast.setText("正在进行注册...");
                        toast.show();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        toast.setText(R.string.tip_register_error);
                        toast.show();
                        Log.i("Register", e.toString());
                    }

                    @Override
                    public void onResponse(UserStatus response) {
                        // 0:用户名存在，1:注册成功
                        switch (response.status) {
                            case 0:
                                toast.cancel();
                                tvErrorTip.setVisibility(View.VISIBLE);
                                tvErrorTip.setText(R.string.tip_username_already);
                                break;
                            case 1:
                                tvErrorTip.setVisibility(View.GONE);
                                toast.setText(R.string.tip_register_success);
                                toast.show();
                                // 将数据传回
                                Intent data = new Intent();
                                data.putExtra(LoginActivity.USERNAME, username);
                                data.putExtra(LoginActivity.PASSWORD, password);
                                setResult(RESULT_OK, data);
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
