package com.wangsy.ouraccounts.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.ChartListAdapter;
import com.wangsy.ouraccounts.model.AccountModel;
import com.wangsy.ouraccounts.model.ChartItemModel;
import com.wangsy.ouraccounts.constants.TableConstants;
import com.wangsy.ouraccounts.ui.ChartAccountListActivity;
import com.wangsy.ouraccounts.ui.MainActivity;
import com.wangsy.ouraccounts.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账目记录统计图
 * <p/>
 * Created by wangsy on 15/10/26.
 */
public class ReportChartFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart reportPieChart;
    private ArrayList<Integer> colors = new ArrayList<>();

    // 类型
    private List<String> chartDataTypes = new ArrayList<>();
    // 百分比
    private List<Float> chartDataPercents = new ArrayList<>();
    // 列表数据
    private List<ChartItemModel> chartDataList = new ArrayList<>();

    private ChartListAdapter adapter;

    // 总金额
    private float totalAmount;

    // 查询记录类别、图标
    private static final String QUERY_TYPES = "select distinct " + TableConstants.TYPE
            + " , " + TableConstants.ICONIMAGENAME + " from " + TableConstants.TABLENAME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_chart, container, false);

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.tab_report_chart);

        initListView(view);
        initPieChartView(view);

        return view;
    }

    private void initListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.id_chart_list);
        adapter = new ChartListAdapter(getActivity(), chartDataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChartAccountListActivity.class);
                intent.putExtra(ChartAccountListActivity.EXTRA_TYPE_NAME, chartDataList.get(position).type);
                startActivity(intent);
            }
        });
    }

    private void initPieChartView(View view) {
        reportPieChart = (PieChart) view.findViewById(R.id.id_report_data_chart);

        // 在饼图中心显示文字，饼块上不显示文字
        reportPieChart.setDrawCenterText(true);
        reportPieChart.setDrawSliceText(false);
        reportPieChart.setCenterTextSize(16f);
        reportPieChart.setDescription("");

        // 显示百分比
        reportPieChart.setUsePercentValues(true);

        // 空心圆
        reportPieChart.setDrawHoleEnabled(true);
        reportPieChart.setHoleColorTransparent(true);
        // 空心圆边界
        reportPieChart.setTransparentCircleColor(Color.WHITE);
        reportPieChart.setTransparentCircleAlpha(110);
        // 空心圆半径
        reportPieChart.setHoleRadius(58f);
        reportPieChart.setTransparentCircleRadius(61f);

        // 禁止手动旋转
        reportPieChart.setRotationAngle(0);
        reportPieChart.setRotationEnabled(false);
        reportPieChart.setHighlightEnabled(false);

        reportPieChart.setOnChartValueSelectedListener(this);

        initPieChartData();

        Legend l = reportPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void initPieChartData() {
        // add a lot of colors
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());

        new QueryDataTask().execute();
    }

    /**
     * 异步查询数据
     */
    private class QueryDataTask extends AsyncTask<Void, Void, Map<String, List>> {

        private ProgressDialog dialog;

        @Override
        protected Map<String, List> doInBackground(Void... params) {
            Map<String, List> result = new HashMap<>();

            List<String> types = new ArrayList<>();
            List<Float> percents = new ArrayList<>();
            List<ChartItemModel> datas = new ArrayList<>();

            // 所有记录总金额
            totalAmount = DataSupport.sum(AccountModel.class, TableConstants.AMOUNT, float.class);

            // 查询类别、图标、类别总金额
            Cursor cursor = DataSupport.findBySQL(QUERY_TYPES);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String type = cursor.getString(cursor.getColumnIndex(TableConstants.TYPE));
                    String iconImageName = cursor.getString(cursor.getColumnIndex(TableConstants.ICONIMAGENAME));
                    float sum = DataSupport.where(TableConstants.TYPE + " = ?", type)
                            .sum(AccountModel.class, TableConstants.AMOUNT, float.class);

                    types.add(type);
                    percents.add(sum / totalAmount);

                    ChartItemModel chartItemModel = new ChartItemModel();
                    chartItemModel.type = type;
                    chartItemModel.percent = Utils.convertFloatToPercent(sum / totalAmount);
                    chartItemModel.iconImageName = iconImageName;
                    chartItemModel.sum = sum;
                    datas.add(chartItemModel);

                } while (cursor.moveToNext());
                cursor.close();
            }

            result.put("types", types);
            result.put("percents", percents);
            result.put("datas", datas);

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "", "正在加载，请稍等...");
        }

        @Override
        protected void onPostExecute(Map<String, List> result) {
            dialog.dismiss();

            chartDataTypes.clear();
            chartDataPercents.clear();
            chartDataList.clear();

            chartDataTypes.addAll(result.get("types"));
            chartDataPercents.addAll(result.get("percents"));
            chartDataList.addAll(result.get("datas"));
            Collections.sort(chartDataList, new SortBySum());

            adapter.notifyDataSetChanged();
            setPieChartData();
        }
    }

    private void setPieChartData() {
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < chartDataPercents.size(); i++) {
            yValues.add(new Entry(chartDataPercents.get(i), i));
        }

        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < chartDataTypes.size(); i++) {
            xValues.add(chartDataTypes.get(i));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);
        dataSet.setColors(colors);

        PieData data = new PieData(xValues, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        reportPieChart.setCenterText("总金额\n" + totalAmount + "\n元");
        reportPieChart.setData(data);
        reportPieChart.highlightValues(null);
        reportPieChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null) {
            return;
        }
        Log.i("VAL SELECTED", "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    /**
     * 接收刷新数据的广播
     */
    private class RefreshDataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new QueryDataTask().execute();
        }
    }

    private RefreshDataBroadcastReceiver refreshDataBroadcastReceiver;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 注册广播
        refreshDataBroadcastReceiver = new RefreshDataBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.REFRESH_DATA_BROADCAST_INTENT_FILTER);
        activity.registerReceiver(refreshDataBroadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshDataBroadcastReceiver);
    }

    /**
     * 根据总金额进行排序
     */
    class SortBySum implements Comparator<ChartItemModel> {
        @Override
        public int compare(ChartItemModel lhs, ChartItemModel rhs) {
            float sum1 = lhs.sum;
            float sum2 = rhs.sum;
            if (sum1 > sum2) {
                return -1;
            } else if (sum1 == sum2) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
