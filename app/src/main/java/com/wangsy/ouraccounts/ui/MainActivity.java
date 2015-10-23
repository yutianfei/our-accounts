package com.wangsy.ouraccounts.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.fragment.AddFragment;
import com.wangsy.ouraccounts.fragment.ReportFragment;
import com.wangsy.ouraccounts.fragment.SettingFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // 每个标签对应的索引
    public static final int TAB_ADD_INDEX = 0;
    public static final int TAB_REPORT_INDEX = 1;
    public static final int TAB_SETTING_INDEX = 2;

    //标签图标
    private ImageView imgAddView;
    private ImageView imgReportView;
    private ImageView imgSettingView;

    // 标签名称
    private TextView tvAddName;
    private TextView tvReportName;
    private TextView tvSettingName;

    // 标签页fragment
    private Fragment mTabAddFragment;
    private Fragment mTabReportFragment;
    private Fragment mTabSettingFragment;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        fm = getSupportFragmentManager();

        //默认第一次进入时显示第一个标签页
        setSelectFragment(TAB_ADD_INDEX);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tab_add:
                setSelectFragment(TAB_ADD_INDEX);
                break;
            case R.id.id_tab_report:
                setSelectFragment(TAB_REPORT_INDEX);
                break;
            case R.id.id_tab_setting:
                setSelectFragment(TAB_SETTING_INDEX);
                break;
        }
    }

    private void setSelectFragment(int tabIndex) {
        FragmentTransaction ft = fm.beginTransaction();

        // 清除标签状态，将标签图标和标签名称设置为未选中的状态
        resetTabStates();

        // 隐藏所有标签页
        hideAllFragments(ft);

        // 设置标签状态和显示的标签页
        switch (tabIndex) {
            case TAB_ADD_INDEX:
                imgAddView.setImageResource(R.mipmap.selected_add);
                tvAddName.setTextColor(getResources().getColor(R.color.color_text_selected));
                if (mTabAddFragment == null) {
                    mTabAddFragment = new AddFragment();
                    ft.add(R.id.tab_container, mTabAddFragment);
                } else {
                    ft.show(mTabAddFragment);
                }
                break;

            case TAB_REPORT_INDEX:
                imgReportView.setImageResource(R.mipmap.selected_report);
                tvReportName.setTextColor(getResources().getColor(R.color.color_text_selected));
                if (mTabReportFragment == null) {
                    mTabReportFragment = new ReportFragment();
                    ft.add(R.id.tab_container, mTabReportFragment);
                } else {
                    ft.show(mTabReportFragment);
                }
                break;

            case TAB_SETTING_INDEX:
                imgSettingView.setImageResource(R.mipmap.selected_setting);
                tvSettingName.setTextColor(getResources().getColor(R.color.color_text_selected));
                if (mTabSettingFragment == null) {
                    mTabSettingFragment = new SettingFragment();
                    ft.add(R.id.tab_container, mTabSettingFragment);
                } else {
                    ft.show(mTabSettingFragment);
                }
                break;

            default:
                break;
        }
        ft.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (mTabAddFragment != null) {
            ft.hide(mTabAddFragment);
        }
        if (mTabReportFragment != null) {
            ft.hide(mTabReportFragment);
        }
        if (mTabSettingFragment != null) {
            ft.hide(mTabSettingFragment);
        }
    }

    private void resetTabStates() {
        imgAddView.setImageResource(R.mipmap.normal_add);
        imgReportView.setImageResource(R.mipmap.normal_report);
        imgSettingView.setImageResource(R.mipmap.normal_setting);

        tvAddName.setTextColor(getResources().getColor(R.color.color_text_normal));
        tvReportName.setTextColor(getResources().getColor(R.color.color_text_normal));
        tvSettingName.setTextColor(getResources().getColor(R.color.color_text_normal));
    }

    private void initViews() {
        initTabViews();
        initTabIconViews();
        initTabNameViews();
    }

    private void initTabViews() {
        LinearLayout mAddTab = (LinearLayout) findViewById(R.id.id_tab_add);
        LinearLayout mReportTab = (LinearLayout) findViewById(R.id.id_tab_report);
        LinearLayout mSettingTab = (LinearLayout) findViewById(R.id.id_tab_setting);
        mAddTab.setOnClickListener(this);
        mReportTab.setOnClickListener(this);
        mSettingTab.setOnClickListener(this);
    }

    private void initTabIconViews() {
        imgAddView = (ImageView) findViewById(R.id.id_tab_add_icon);
        imgReportView = (ImageView) findViewById(R.id.id_tab_report_icon);
        imgSettingView = (ImageView) findViewById(R.id.id_tab_setting_icon);
    }

    private void initTabNameViews() {
        tvAddName = (TextView) findViewById(R.id.id_tab_add_name);
        tvReportName = (TextView) findViewById(R.id.id_tab_report_name);
        tvSettingName = (TextView) findViewById(R.id.id_tab_setting_name);
    }

}
