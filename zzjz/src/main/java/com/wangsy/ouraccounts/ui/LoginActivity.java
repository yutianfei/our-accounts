package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;

/**
 * 登陆，注册
 * <p/>
 * Created by wangsy on 15/11/5.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tvTitle = (TextView) findViewById(R.id.id_title);
        tvTitle.setText(R.string.title_login);

        // 左侧按钮：取消登录，返回
        initButtonLeft();

        // 登录按钮
        initButtonLogin();
    }

    private void initButtonLogin() {
        Button btnLogin = (Button) findViewById(R.id.id_button_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    // 登录
    private void login() {
        // TODO
    }

    private void initButtonLeft() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
