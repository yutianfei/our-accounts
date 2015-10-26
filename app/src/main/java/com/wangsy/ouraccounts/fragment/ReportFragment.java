package com.wangsy.ouraccounts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;

/**
 * 列表
 * 统计图
 * <p/>
 * Created by wangsy on 15/10/22.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {

    // 每个标签对应的索引
    public static final int TAB_LIST_INDEX = 0;
    public static final int TAB_CHART_INDEX = 1;

    // 标签名称
    private TextView tvListName;
    private TextView tvChartName;

    // 标签下划线
    private View viewListUnderline;
    private View viewChartUnderline;

    // 标签页fragment
    private Fragment mListFragment;
    private Fragment mChartFragment;

    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, null);

        view.findViewById(R.id.id_title).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.id_report_title).setVisibility(View.VISIBLE);

        initViews(view);
        fm = getChildFragmentManager();

        //默认第一次进入时显示第一个标签页
        setSelectFragment(TAB_LIST_INDEX);

        return view;
    }

    private void setSelectFragment(int index) {
        FragmentTransaction ft = fm.beginTransaction();

        // 清除标签状态
        resetTabStates();

        // 隐藏所有标签页
        hideAllFragments(ft);

        // 设置标签状态和显示的标签页
        switch (index) {
            case TAB_LIST_INDEX:
                tvListName.setTextColor(getResources().getColor(android.R.color.white));
                viewListUnderline.setVisibility(View.VISIBLE);
                if (mListFragment == null) {
                    mListFragment = new ReportListFragment();
                    ft.add(R.id.tab_container, mListFragment);
                } else {
                    ft.show(mListFragment);
                }
                break;

            case TAB_CHART_INDEX:
                tvChartName.setTextColor(getResources().getColor(android.R.color.white));
                viewChartUnderline.setVisibility(View.VISIBLE);
                if (mChartFragment == null) {
                    mChartFragment = new ReportChartFragment();
                    ft.add(R.id.tab_container, mChartFragment);
                } else {
                    ft.show(mChartFragment);
                }
                break;

            default:
                break;
        }
        ft.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (mListFragment != null) {
            ft.hide(mListFragment);
        }
        if (mChartFragment != null) {
            ft.hide(mChartFragment);
        }
    }

    private void resetTabStates() {
        tvListName.setTextColor(getResources().getColor(R.color.color_text_normal));
        tvChartName.setTextColor(getResources().getColor(R.color.color_text_normal));
        viewListUnderline.setVisibility(View.GONE);
        viewChartUnderline.setVisibility(View.GONE);
    }

    private void initViews(View view) {
        tvListName = (TextView) view.findViewById(R.id.id_report_list_title);
        tvChartName = (TextView) view.findViewById(R.id.id_report_chart_title);
        viewListUnderline = view.findViewById(R.id.id_report_list_underline);
        viewChartUnderline = view.findViewById(R.id.id_report_chart_underline);

        LinearLayout mTabList = (LinearLayout) view.findViewById(R.id.id_report_list);
        LinearLayout mTabChart = (LinearLayout) view.findViewById(R.id.id_report_chart);
        mTabList.setOnClickListener(this);
        mTabChart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_report_list:
                setSelectFragment(TAB_LIST_INDEX);
                break;
            case R.id.id_report_chart:
                setSelectFragment(TAB_CHART_INDEX);
                break;
        }
    }
}
