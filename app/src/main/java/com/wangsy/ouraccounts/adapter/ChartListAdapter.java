package com.wangsy.ouraccounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.model.ChartItemModel;
import com.wangsy.ouraccounts.utils.Util;

import java.util.List;

/**
 * 图表下方的列表数据
 * <p/>
 * Created by wangsy on 15/10/30.
 */
public class ChartListAdapter extends BaseAdapter {

    private List<ChartItemModel> chartDataList;
    private Context context;

    public ChartListAdapter(Context context, List<ChartItemModel> chartDataList) {
        this.context = context;
        this.chartDataList = chartDataList;
    }

    @Override
    public int getCount() {
        return chartDataList.size();
    }

    @Override
    public ChartItemModel getItem(int position) {
        return chartDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChartItemModel model = getItem(position);
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_chart_list_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgViewIcon = (ImageView) convertView.findViewById(R.id.id_chart_icon_img);
            viewHolder.tvChartType = (TextView) convertView.findViewById(R.id.id_chart_type);
            viewHolder.tvChartSumAmount = (TextView) convertView.findViewById(R.id.id_chart_sum_amount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgViewIcon.setImageResource(Util.getImageResourceId(context, model.iconImageName));
        viewHolder.tvChartType.setText(model.type);
        viewHolder.tvChartSumAmount.setText(model.sum + "");

        if ("收入".equals(model.type)) {
            viewHolder.tvChartSumAmount.setTextColor(context.getResources().getColor(R.color.color_money_in));
        } else {
            viewHolder.tvChartSumAmount.setTextColor(context.getResources().getColor(R.color.color_money_out));
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvChartType, tvChartSumAmount;
        ImageView imgViewIcon;
    }
}
