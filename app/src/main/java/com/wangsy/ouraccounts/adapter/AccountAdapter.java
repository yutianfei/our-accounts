package com.wangsy.ouraccounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;

import java.util.List;

/**
 * Created by wangsy on 15/10/27.
 */
public class AccountAdapter extends BaseAdapter {
    private List<String> strs;
    private Context context;

    public AccountAdapter(List<String> strs, Context context) {
        this.strs = strs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return strs.size();
    }

    @Override
    public Object getItem(int i) {
        return strs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_account_layout, viewGroup, false);
            viewHolder.tvStr = (TextView) view.findViewById(R.id.tvStr);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvStr.setText(strs.get(i));
        return view;
    }

    static class ViewHolder {
        TextView tvStr;
    }
}
