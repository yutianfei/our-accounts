package com.wangsy.ouraccounts.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsy.ouraccounts.R;

/**
 * app版本更新
 * <p/>
 * Created by wangsy on 15/11/3.
 */
public class AppUpdateUtils {
    /**
     * 检查更新
     */
    public static void checkAppVersionUpdate(Context context) {
        // TODO 从网络检测最新版本信息
        int lastedVersionCode = 2;
        String lastedVersionName = context.getResources().getString(R.string.setting_about_version) + "1.2.0";


        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            int currentVersionCode = info.versionCode;
            String currentVersionName = context.getResources().getString(R.string.setting_about_version) + info.versionName;

            if (lastedVersionCode == currentVersionCode) {
                Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_SHORT).show();
            } else {
                showChooseUpdateDialog(context, currentVersionName, lastedVersionName);
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getPackageName", e.getMessage());
        }
    }

    private static void showChooseUpdateDialog(final Context context, String currentVersionName, String lastedVersionName) {
        final Dialog dialog = new Dialog(context, R.style.style_dialog_common);
        View view = View.inflate(context, R.layout.dialog_common, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.id_dialog_title);
        tvTitle.setText("是否更新？");
        TextView tvMessage = (TextView) view.findViewById(R.id.id_dialog_message);
        tvMessage.setText(String.format(context.getResources().getString(R.string.update_version_string),
                currentVersionName, lastedVersionName));

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
                // TODO 进行更新
                Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
