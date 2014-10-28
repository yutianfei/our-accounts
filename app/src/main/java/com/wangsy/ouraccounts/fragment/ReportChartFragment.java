package com.wangsy.ouraccounts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangsy.ouraccounts.R;

/**
 * 账目记录统计图
 * <p/>
 * Created by wangsy on 15/10/26.
 */
public class ReportChartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_chart, container, false);

        return view;
    }
}
