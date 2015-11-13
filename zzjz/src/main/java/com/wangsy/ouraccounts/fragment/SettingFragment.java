package com.wangsy.ouraccounts.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.constants.UrlConstants;
import com.wangsy.ouraccounts.model.VersionModel;
import com.wangsy.ouraccounts.ui.AboutActivity;
import com.wangsy.ouraccounts.ui.ChangePasswordActivity;
import com.wangsy.ouraccounts.ui.FeedbackActivity;
import com.wangsy.ouraccounts.ui.LoginActivity;
import com.wangsy.ouraccounts.utils.NetworkUtils;
import com.wangsy.ouraccounts.utils.OkHttpClientManager;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 设置
 * <p/>
 * Created by wangsy on 15/10/22.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_FOR_LOGIN = 1;

    // 用户名
    private TextView tvUsername;
    // 退出登录
    private Button btnLogout;
    // 修改密码
    private RelativeLayout settingChangePassword;

    private String username;

    // Toast提示
    private Toast toast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(LoginActivity.USER_SharedPreferences, Context.MODE_PRIVATE);
        username = sp.getString(LoginActivity.USERNAME, "");

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.title_setting);

        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        tvUsername = (TextView) view.findViewById(R.id.id_user_name);
        tvUsername.setText(TextUtils.isEmpty(username) ? getString(R.string.click_to_login) : username);

        // 用户头像
        ImageView ivUserImage = (ImageView) view.findViewById(R.id.id_user_image);
        ivUserImage.setOnClickListener(this);

        // 意见反馈
        View settingFeedback = view.findViewById(R.id.id_setting_feedback);
        settingFeedback.setOnClickListener(this);

        // 推荐好友
        View settingRecommend = view.findViewById(R.id.id_setting_share);
        settingRecommend.setOnClickListener(this);

        // 关于
        View settingAbout = view.findViewById(R.id.id_setting_about);
        settingAbout.setOnClickListener(this);

        // 更新
        View settingUpdate = view.findViewById(R.id.id_setting_update);
        settingUpdate.setOnClickListener(this);

        // 退出当前账号
        btnLogout = (Button) view.findViewById(R.id.id_button_logout);
        btnLogout.setVisibility(TextUtils.isEmpty(username) ? View.GONE : View.VISIBLE);
        btnLogout.setOnClickListener(this);

        // 修改密码
        settingChangePassword = (RelativeLayout) view.findViewById(R.id.id_setting_user_change_password);
        settingChangePassword.setVisibility(TextUtils.isEmpty(username) ? View.GONE : View.VISIBLE);
        settingChangePassword.setOnClickListener(this);

        // 用户协议
        View settingUserAgreement = view.findViewById(R.id.id_setting_user_agreement);
        settingUserAgreement.setOnClickListener(this);
        settingUserAgreement.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_setting_feedback:
                gotoFeedbackActivity();
                break;
            case R.id.id_setting_share:
                showShareDialog();
                break;
            case R.id.id_setting_about:
                gotoAboutActivity();
                break;
            case R.id.id_setting_update:
                checkAppUpdate();
                break;
            case R.id.id_setting_user_agreement:
                gotoUserAgreementActivity();
                break;
            case R.id.id_user_image:
                if (TextUtils.isEmpty(username)) {
                    gotoLoginActivity();
                }
                break;
            case R.id.id_button_logout:
                logout();
                break;
            case R.id.id_setting_user_change_password:
                gotoChangePasswordActivity();
            default:
                break;
        }
    }

    /**
     * 前往修改密码
     */
    private void gotoChangePasswordActivity() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    private void logout() {
        // 清除保存的账号密码
        SharedPreferences sp = getActivity().getSharedPreferences(LoginActivity.USER_SharedPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(LoginActivity.USERNAME);
        editor.remove(LoginActivity.PASSWORD);
        if (editor.commit()) {
            // 设置相应的界面元素
            settingChangePassword.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
            username = "";
            tvUsername.setText(R.string.click_to_login);
        }
    }

    /**
     * 前往登录
     */
    private void gotoLoginActivity() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_FOR_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_FOR_LOGIN) {
            String username = data.getStringExtra(LoginActivity.USERNAME);
            tvUsername.setText(username);
            btnLogout.setVisibility(View.VISIBLE);
            settingChangePassword.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 信息反馈界面
     */
    private void gotoFeedbackActivity() {
        Intent feedbackIntent = new Intent(getActivity(), FeedbackActivity.class);
        startActivity(feedbackIntent);
    }

    /**
     * 关于app界面
     */
    private void gotoAboutActivity() {
        Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
        startActivity(aboutIntent);
    }

    /**
     * 检查更新
     */
    private void checkAppUpdate() {
        // 检查网络是否可用，如果不可用，取消更新操作
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            toast.setText(R.string.tip_network_is_connected);
            toast.show();
            return;
        }
        // 从网络检测最新版本信息
        OkHttpClientManager.getAsyn(UrlConstants.HTTP_CHECK_UPDATE, new OkHttpClientManager.ResultCallback<VersionModel>() {
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                toast.setText("正在检查更新...");
                toast.show();
            }

            @Override
            public void onError(Request request, Exception e) {
                toast.setText(R.string.tip_check_update_error);
                toast.show();
                Log.i("CheckUpdate", e.toString());
            }

            @Override
            public void onResponse(VersionModel response) {
                if (response == null) {
                    toast.setText("暂时没有新版本哦！");
                    toast.show();
                } else {
                    checkUpdate(response);
                }
            }
        });
    }

    private void checkUpdate(VersionModel lastedVersion) {
        if (lastedVersion != null) {
            PackageManager pm = getActivity().getPackageManager();
            try {
                PackageInfo info = pm.getPackageInfo(getActivity().getPackageName(), 0);
                int currentVersionCode = info.versionCode;
                String currentVersionName = getActivity().getResources().getString(R.string.setting_about_version) + info.versionName;

                if (lastedVersion.versionCode == currentVersionCode) {
                    toast.setText(R.string.tip_current_lasted);
                    toast.show();
                } else {
                    toast.cancel();
                    chooseUpdateDialog(currentVersionName, lastedVersion);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("getPackageName", e.getMessage());
            }
        }
    }

    private void chooseUpdateDialog(String currentVersionName, final VersionModel lastedVersion) {
        final Dialog dialog = new Dialog(getActivity(), R.style.style_dialog_common);
        View view = View.inflate(getActivity(), R.layout.dialog_common, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.id_dialog_title);
        tvTitle.setText(R.string.tip_choose_update);
        TextView tvMessage = (TextView) view.findViewById(R.id.id_dialog_message);
        String updateMessage = lastedVersion.updateMessage == null ? "" : "\n\n" + lastedVersion.updateMessage;
        tvMessage.setText(String.format(getActivity().getResources().getString(R.string.update_version_string),
                currentVersionName, lastedVersion.versionName, updateMessage));

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
                // 调用浏览器进行下载
                toast.setText(R.string.tip_going_download);
                toast.show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lastedVersion.downloadUrl));
                startActivity(intent);

                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    /**
     * 分享
     */
    private void showShareDialog() {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(UrlConstants.HTTP_APP_INDEX);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(getString(R.string.share_text));
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");
        // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段
        oks.setImageUrl(UrlConstants.HTTP_ICON_IMAGE);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(UrlConstants.HTTP_APP_INDEX);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(getString(R.string.share_text));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(UrlConstants.HTTP_APP_INDEX);

        // 启动分享GUI
        oks.show(getActivity());
    }

    /**
     * 用户协议界面
     */
    private void gotoUserAgreementActivity() {
        // TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpClientManager.cancelTag(this);
    }
}
