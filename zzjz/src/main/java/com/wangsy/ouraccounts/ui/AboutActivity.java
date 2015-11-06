package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;

/**
 * 关于记一笔
 * <p/>
 * Created by wangsy on 15/11/2.
 */
public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // 标题
        TextView tvTitle = (TextView) findViewById(R.id.id_title);
        tvTitle.setText(R.string.setting_about);

        // 显示左侧按钮：返回
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 版本号
        TextView tvVersion = (TextView) findViewById(R.id.id_app_version);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(this.getPackageName(), 0);
            String version = getResources().getString(R.string.setting_about_version) + info.versionName;
            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getPackageName", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
