package com.wangsy.ouraccounts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.ui.AboutActivity;
import com.wangsy.ouraccounts.ui.FeedbackActivity;

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
        View settingRecommend = view.findViewById(R.id.id_setting_recommend);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_setting_feedback:
                gotoFeedbackActivity();
                break;
            case R.id.id_setting_recommend:
                showRecommendDialog();
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

    private void gotoFeedbackActivity() {
        Intent feedbackIntent = new Intent(getActivity(), FeedbackActivity.class);
        startActivity(feedbackIntent);
    }

    private void gotoAboutActivity() {
        Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
        startActivity(aboutIntent);
    }

    private void gotoUserAgreementActivity() {
    }

    private void showRecommendDialog() {
    }

    private void checkUpdate() {
    }


}
