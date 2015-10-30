package com.wangsy.ouraccounts.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;

/**
 * 设置
 * <p/>
 * Created by wangsy on 15/10/22.
 */
public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.title_setting);

        return view;
    }
}
