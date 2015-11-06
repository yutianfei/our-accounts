package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.wangsy.ouraccounts.Constants;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.utils.NetworkUtils;
import com.wangsy.ouraccounts.utils.OkHttpClientManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册
 * <p/>
 * Created by wangsy on 15/11/6.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

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
            Toast.makeText(this, R.string.tip_network_is_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(LoginActivity.USERNAME, username);
        params.put(LoginActivity.PASSWORD, password);
        OkHttpClientManager.postAsyn(Constants.HTTP_USER_REGISTER, params,
                new OkHttpClientManager.ResultCallback<Integer>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(RegisterActivity.this, R.string.tip_register_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Integer response) {
                        // 0:用户名存在，1:注册成功
                        switch (response) {
                            case 0:
                                tvErrorTip.setVisibility(View.VISIBLE);
                                tvErrorTip.setText(R.string.tip_username_already);
                                break;
                            case 1:
                                tvErrorTip.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, R.string.tip_register_success, Toast.LENGTH_SHORT).show();
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
}
