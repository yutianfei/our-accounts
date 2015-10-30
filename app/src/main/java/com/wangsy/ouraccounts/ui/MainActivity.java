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
import com.wangsy.ouraccounts.fragment.ReportChartFragment;
import com.wangsy.ouraccounts.fragment.ReportListFragment;
import com.wangsy.ouraccounts.fragment.SettingFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static final String REFRESH_DATA_BROADCAST_INTENT_FILTER = "com.refreshData";

    // 每个标签对应的索引
    public static final int TAB_ADD_INDEX = 0;
    public static final int TAB_REPORT_LIST_INDEX = 1;
    public static final int TAB_REPORT_CHART_INDEX = 2;
    public static final int TAB_SETTING_INDEX = 3;

    //标签图标
    private ImageView imgAddView;
    private ImageView imgReportListView;
    private ImageView imgReportChartView;
    private ImageView imgSettingView;

    // 标签名称
    private TextView tvAddName;
    private TextView tvReportListName;
    private TextView tvReportChartName;
    private TextView tvSettingName;

    // 标签页fragment
    private Fragment mTabAddFragment;
    private Fragment mTabReportListFragment;
    private Fragment mTabReportChartFragment;
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
            case R.id.id_tab_report_list:
                setSelectFragment(TAB_REPORT_LIST_INDEX);
                break;
            case R.id.id_tab_report_chart:
                setSelectFragment(TAB_REPORT_CHART_INDEX);
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

            case TAB_REPORT_LIST_INDEX:
                imgReportListView.setImageResource(R.mipmap.selected_list);
                tvReportListName.setTextColor(getResources().getColor(R.color.color_text_selected));
                if (mTabReportListFragment == null) {
                    mTabReportListFragment = new ReportListFragment();
                    ft.add(R.id.tab_container, mTabReportListFragment);
                } else {
                    ft.show(mTabReportListFragment);
                }
                break;

            case TAB_REPORT_CHART_INDEX:
                imgReportChartView.setImageResource(R.mipmap.selected_report);
                tvReportChartName.setTextColor(getResources().getColor(R.color.color_text_selected));
                if (mTabReportChartFragment == null) {
                    mTabReportChartFragment = new ReportChartFragment();
                    ft.add(R.id.tab_container, mTabReportChartFragment);
                } else {
                    ft.show(mTabReportChartFragment);
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
        if (mTabReportListFragment != null) {
            ft.hide(mTabReportListFragment);
        }
        if (mTabReportChartFragment != null) {
            ft.hide(mTabReportChartFragment);
        }
        if (mTabSettingFragment != null) {
            ft.hide(mTabSettingFragment);
        }
    }

    private void resetTabStates() {
        imgAddView.setImageResource(R.mipmap.normal_add);
        imgReportListView.setImageResource(R.mipmap.normal_list);
        imgReportChartView.setImageResource(R.mipmap.normal_report);
        imgSettingView.setImageResource(R.mipmap.normal_setting);

        tvAddName.setTextColor(getResources().getColor(R.color.color_text_normal));
        tvReportListName.setTextColor(getResources().getColor(R.color.color_text_normal));
        tvReportChartName.setTextColor(getResources().getColor(R.color.color_text_normal));
        tvSettingName.setTextColor(getResources().getColor(R.color.color_text_normal));
    }

    private void initViews() {
        initTabViews();
        initTabIconViews();
        initTabNameViews();
    }

    private void initTabViews() {
        LinearLayout mAddTab = (LinearLayout) findViewById(R.id.id_tab_add);
        LinearLayout mReportListTab = (LinearLayout) findViewById(R.id.id_tab_report_list);
        LinearLayout mReportChartTab = (LinearLayout) findViewById(R.id.id_tab_report_chart);
        LinearLayout mSettingTab = (LinearLayout) findViewById(R.id.id_tab_setting);
        mAddTab.setOnClickListener(this);
        mReportListTab.setOnClickListener(this);
        mReportChartTab.setOnClickListener(this);
        mSettingTab.setOnClickListener(this);
    }

    private void initTabIconViews() {
        imgAddView = (ImageView) findViewById(R.id.id_tab_add_icon);
        imgReportListView = (ImageView) findViewById(R.id.id_tab_report_list_icon);
        imgReportChartView = (ImageView) findViewById(R.id.id_tab_report_chart_icon);
        imgSettingView = (ImageView) findViewById(R.id.id_tab_setting_icon);
    }

    private void initTabNameViews() {
        tvAddName = (TextView) findViewById(R.id.id_tab_add_name);
        tvReportListName = (TextView) findViewById(R.id.id_tab_report_list_name);
        tvReportChartName = (TextView) findViewById(R.id.id_tab_report_chart_name);
        tvSettingName = (TextView) findViewById(R.id.id_tab_setting_name);
    }

}
