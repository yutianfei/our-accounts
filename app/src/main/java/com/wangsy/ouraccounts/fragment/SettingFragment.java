package com.wangsy.ouraccounts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.ui.AboutActivity;
import com.wangsy.ouraccounts.ui.FeedbackActivity;
import com.wangsy.ouraccounts.utils.AppUpdateUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 设置
 * <p/>
 * Created by wangsy on 15/10/22.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.title_setting);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
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
                checkUpdate();
                break;
            case R.id.id_setting_user_agreement:
                gotoUserAgreementActivity();
                break;
            default:
                break;
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
    private void checkUpdate() {
        AppUpdateUtils.checkAppVersionUpdate(getActivity());
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
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("周周记账，一个使用方便的记账App，随时随地满足大家的记账需求哦！（＾_＾）");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(getActivity());

    }

    /**
     * 用户协议界面
     */
    private void gotoUserAgreementActivity() {
    }

}
