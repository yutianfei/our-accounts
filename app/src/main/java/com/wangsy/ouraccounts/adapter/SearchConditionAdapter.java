package com.wangsy.ouraccounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.model.SearchTextModel;

import java.util.List;

/**
 * 快捷搜索：类型
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class SearchConditionAdapter extends BaseAdapter {

    private Context context;
    private List<SearchTextModel> list;

    public SearchConditionAdapter(Context context, List<SearchTextModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SearchTextModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchTextModel model = getItem(position);
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvSearch = (TextView) convertView.findViewById(R.id.id_search_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSearch.setText(model.text);
        viewHolder.tvSearch.setTextColor(context.getResources().getColor(model.textColor));
        viewHolder.tvSearch.setBackgroundResource(model.backgroundResourceId);
        return convertView;
    }

    private class ViewHolder {
        TextView tvSearch;
    }
}
