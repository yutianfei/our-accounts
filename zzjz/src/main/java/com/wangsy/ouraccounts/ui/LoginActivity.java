package com.wangsy.ouraccounts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
 * 登陆，注册
 * <p/>
 * Created by wangsy on 15/11/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_FOR_REGISTER = 1;
    public static final String USER_SharedPreferences = "user";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    // 用户名
    private EditText etUsername;
    // 密码框
    private EditText etPassword;
    // 切换密码可见性
    private TextView tvTogglePasswordVisibility;
    // 密码是否可见：默认可见，不隐藏
    private boolean isPasswordHidden = false;
    // 错误提示
    private TextView tvErrorTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tvTitle = (TextView) findViewById(R.id.id_title);
        tvTitle.setText(R.string.title_login);

        initViews();
    }

    private void initViews() {
        etUsername = (EditText) findViewById(R.id.id_login_username);
        etPassword = (EditText) findViewById(R.id.id_login_password);
        tvErrorTip = (TextView) findViewById(R.id.id_error_tip);

        // 左侧按钮：取消登录，返回
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        // 登录按钮
        Button btnLogin = (Button) findViewById(R.id.id_button_login);
        btnLogin.setOnClickListener(this);

        // 切换密码可见性
        tvTogglePasswordVisibility = (TextView) findViewById(R.id.id_password_visibility);
        tvTogglePasswordVisibility.setOnClickListener(this);

        // 注册
        TextView tvRegister = (TextView) findViewById(R.id.id_login_register);
        tvRegister.setOnClickListener(this);

        // 忘记密码
        TextView tvForgetPassword = (TextView) findViewById(R.id.id_password_forget);
        tvForgetPassword.setOnClickListener(this);

        // 弹出软键盘
        Utils.showKeyBoard(etUsername);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_title_left_btn:
                finish();
                break;
            case R.id.id_password_visibility:
                togglePasswordVisibility();
                break;
            case R.id.id_button_login:
                login();
                break;
            case R.id.id_login_register:
                register();
                break;
            case R.id.id_password_forget:
                forgetPassword();
                break;
            default:
                break;
        }
    }

    /**
     * 切换密码可见性
     */
    private void togglePasswordVisibility() {
        isPasswordHidden = !isPasswordHidden;
        if (isPasswordHidden) {
            // 设置密码隐藏
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            // 标签设为可见
            tvTogglePasswordVisibility.setText(R.string.password_see);
        } else {
            // 设置密码可见
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // 标签设为隐藏
            tvTogglePasswordVisibility.setText(R.string.password_hidden);
        }
        etPassword.postInvalidate();
        // 切换后将光标置于末尾
        etPassword.setSelection(etPassword.getText().toString().length());
    }

    /**
     * 登录
     */
    private void login() {
        tvErrorTip.setVisibility(View.GONE);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

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

        // 检查网络是否可用，如果不可用，取消登录
        if (!NetworkUtils.isNetworkAvailable(this)) {
            toast.setText(R.string.tip_network_is_connected);
            toast.show();
            return;
        }

        login(username, password);
    }

    private void login(final String username, final String password) {
        Map<String, String> params = HttpParams.loginParams(username, password);
        OkHttpClientManager.postAsyn(UrlConstants.HTTP_USER_LOGIN, params,
                new OkHttpClientManager.ResultCallback<UserStatus>() {

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        toast.setText("正在登录...");
                        toast.show();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        toast.setText(R.string.tip_login_error);
                        toast.show();
                        Log.i("Login", e.toString());
                    }

                    @Override
                    public void onResponse(UserStatus response) {
                        // 0:用户名不存在，1:密码错误，2:登录成功
                        switch (response.status) {
                            case 0:
                                toast.cancel();
                                tvErrorTip.setVisibility(View.VISIBLE);
                                tvErrorTip.setText(R.string.tip_username_empty);
                                break;
                            case 1:
                                toast.cancel();
                                tvErrorTip.setVisibility(View.VISIBLE);
                                tvErrorTip.setText(R.string.tip_password_error);
                                break;
                            case 2:
                                tvErrorTip.setVisibility(View.GONE);
                                toast.setText(R.string.tip_login_success);
                                toast.show();
                                // 使用SharedPreferences保存数据
                                SharedPreferences sp = getSharedPreferences(USER_SharedPreferences, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(USERNAME, username);
                                editor.putString(PASSWORD, password);
                                if (editor.commit()) {
                                    // 将数据传回
                                    Intent data = new Intent();
                                    data.putExtra(USERNAME, username);
                                    data.putExtra(PASSWORD, password);
                                    setResult(RESULT_OK, data);
                                    // 关闭当前界面
                                    finish();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    /**
     * 注册
     */
    private void register() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerIntent, REQUEST_FOR_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_FOR_REGISTER) {
            String username = data.getStringExtra(USERNAME);
            String password = data.getStringExtra(PASSWORD);
            etUsername.setText(username);
            etUsername.setSelection(username.length());
            etPassword.setText(password);
            etPassword.setSelection(password.length());
            login(username, password);
        }
    }

    /**
     * 忘记密码
     */
    private void forgetPassword() {
        // TODO
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
