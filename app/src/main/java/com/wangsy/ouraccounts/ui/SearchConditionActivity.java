package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.SearchConditionAdapter;
import com.wangsy.ouraccounts.model.DateTimeConstants;
import com.wangsy.ouraccounts.model.SearchTextModel;
import com.wangsy.ouraccounts.model.TypeConstants;
import com.wangsy.ouraccounts.utils.Util;

import java.util.List;

/**
 * 搜索条件选择界面
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class SearchConditionActivity extends Activity implements View.OnClickListener {

    public static final int REQUEST_SET_START_DATE = 0;
    public static final int REQUEST_SET_END_DATE = 1;

    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_START_DATETIME = "extra_start_datetime";
    public static final String EXTRA_END_DATETIME = "extra_end_datetime";

    public static final String EXTRA_SEARCH_FLAG = "extra_search_flag";
    public static final int SEARCH_FLAG_ALL = 0;
    public static final int SEARCH_FLAG_DATETIME = 1;

    private int searchFlag;

    private SearchConditionAdapter datetimeAdapter;
    private SearchConditionAdapter typeAdapter;

    private List<SearchTextModel> datetimeList;
    private List<SearchTextModel> typeList;

    private String strType;
    private String strDatetime;

    private String strStartDatetimePrepare;
    private String strEndDatetimePrepare;

    private String strStartDatetimeCustom;
    private String strEndDatetimeCustom;

    private GridView datetimeGridView;
    private LinearLayout datetimeCustomLayout;
    private TextView tvDatetimeChoose;
    private boolean isDatetimeCustom = true;

    private TextView tvDatetimeStart;
    private TextView tvDatetimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        strType = getIntent().getStringExtra(EXTRA_TYPE);
        searchFlag = getIntent().getIntExtra(EXTRA_SEARCH_FLAG, 0);

        TextView title = (TextView) findViewById(R.id.id_title);
        title.setText(R.string.title_search);

        initViews();
    }

    private void initViews() {
        // 右侧按钮：将搜索条件返回
        initButtonRight();
        // 左侧按钮：取消搜索，返回
        initButtonLeft();
        // 类型列表
        initTypeGridView();
        // 时间列表
        initDatetimeGridView();
        // 自定义时间
        initDatetimeCustom();

        // 切换时间选择
        tvDatetimeChoose = (TextView) findViewById(R.id.id_choose_datetime);
        tvDatetimeChoose.setOnClickListener(this);
    }

    private void initDatetimeCustom() {
        datetimeCustomLayout = (LinearLayout) findViewById(R.id.id_search_datetime_custom);
        tvDatetimeStart = (TextView) findViewById(R.id.id_start_datetime);
        tvDatetimeStart.setOnClickListener(this);
        tvDatetimeEnd = (TextView) findViewById(R.id.id_end_datetime);
        tvDatetimeEnd.setOnClickListener(this);
    }

    private void initTypeGridView() {
        GridView typeGridView = (GridView) findViewById(R.id.id_search_type);
        if (searchFlag == SEARCH_FLAG_DATETIME) {
            findViewById(R.id.id_search_type_tab).setVisibility(View.GONE);
            typeGridView.setVisibility(View.GONE);
        } else {
            typeList = TypeConstants.getTypeList();
            typeAdapter = new SearchConditionAdapter(this, typeList);
            typeGridView.setAdapter(typeAdapter);
            typeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 清除选中状态
                    resetTypeSelectedStates();
                    // 设置选中状态
                    typeList.get(position).textColor = android.R.color.white;
                    typeList.get(position).backgroundResourceId = R.drawable.bg_search_text_selected;
                    typeAdapter.notifyDataSetChanged();
                    // 设置选中的条件
                    strType = typeList.get(position).text;
                }
            });
        }
    }

    private void initDatetimeGridView() {
        datetimeGridView = (GridView) findViewById(R.id.id_search_datetime);
        datetimeList = DateTimeConstants.getDatetimeList();
        datetimeAdapter = new SearchConditionAdapter(this, datetimeList);
        datetimeGridView.setAdapter(datetimeAdapter);
        datetimeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 清除选中状态
                resetDatetimeSelectedStates();
                // 设置选中状态
                datetimeList.get(position).textColor = android.R.color.white;
                datetimeList.get(position).backgroundResourceId = R.drawable.bg_search_text_selected;
                datetimeAdapter.notifyDataSetChanged();
                // 设置选中的条件
                strDatetime = datetimeList.get(position).text;
                convertDatetime();
            }
        });
    }

    // 将选择的时间标签转换为相应的时间
    private void convertDatetime() {
        switch (strDatetime) {
            case DateTimeConstants.ALL:
                strStartDatetimePrepare = "";
                strEndDatetimePrepare = "";
                break;
            case DateTimeConstants.ONE_MONTH:
                String[] oneMonth = Util.getDatetimeStringWithMonths(-1);
                strStartDatetimePrepare = oneMonth[0];
                strEndDatetimePrepare = oneMonth[1];
                break;
            case DateTimeConstants.TWO_MONTH:
                String[] twoMonth = Util.getDatetimeStringWithMonths(-2);
                strStartDatetimePrepare = twoMonth[0];
                strEndDatetimePrepare = twoMonth[1];
                break;
            case DateTimeConstants.THREE_MONTH:
                String[] threeMonth = Util.getDatetimeStringWithMonths(-3);
                strStartDatetimePrepare = threeMonth[0];
                strEndDatetimePrepare = threeMonth[1];
                break;
            default:
                break;
        }
    }

    private void resetDatetimeSelectedStates() {
        for (int i = 0; i < datetimeList.size(); i++) {
            datetimeList.get(i).textColor = R.color.color_text_normal;
            datetimeList.get(i).backgroundResourceId = R.drawable.bg_search_text_normal;
        }
    }

    private void resetTypeSelectedStates() {
        for (int i = 0; i < typeList.size(); i++) {
            typeList.get(i).textColor = R.color.color_text_normal;
            typeList.get(i).backgroundResourceId = R.drawable.bg_search_text_normal;
        }
    }

    private void initButtonRight() {
        ImageButton imgBtnRight = (ImageButton) findViewById(R.id.id_title_right_btn);
        imgBtnRight.setVisibility(View.VISIBLE);
        imgBtnRight.setImageResource(R.mipmap.icon_ok);
        imgBtnRight.setOnClickListener(this);
    }

    private void initButtonLeft() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setImageResource(R.mipmap.icon_back);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_title_left_btn:
                finish();
                break;
            case R.id.id_title_right_btn:
                // 进行搜索
                gotoSearchResultActivity();
                break;
            case R.id.id_choose_datetime:
                // 改变时间选择方式
                changeDatetimeChooseType();
                break;
            case R.id.id_start_datetime:
                // 设置开始时间
                setDatetime(REQUEST_SET_START_DATE);
                break;
            case R.id.id_end_datetime:
                // 设置结束时间
                setDatetime(REQUEST_SET_END_DATE);
                break;
        }
    }

    private void setDatetime(int requestCode) {
        Intent intent = new Intent(this, SetDatetimeDialogActivity.class);
        intent.putExtra(SetDatetimeDialogActivity.EXTRA_DATETIME_FLAG, SetDatetimeDialogActivity.DATETIME_FLAG_NO_TIMEPICKER);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SET_START_DATE:
                    strStartDatetimeCustom = data.getStringExtra(SetDatetimeDialogActivity.EXTRA_DATETIME);
                    tvDatetimeStart.setText(strStartDatetimeCustom);
                    break;
                case REQUEST_SET_END_DATE:
                    strEndDatetimeCustom = data.getStringExtra(SetDatetimeDialogActivity.EXTRA_DATETIME);
                    tvDatetimeEnd.setText(strEndDatetimeCustom);
                    break;
                default:
                    break;
            }
        }
    }

    private void gotoSearchResultActivity() {

        if (TypeConstants.ALL.equals(strType)) {
            strType = "";
        }

        Intent intent = new Intent(SearchConditionActivity.this, SearchResultActivity.class);
        intent.putExtra(EXTRA_TYPE, strType);

        if (!isDatetimeCustom) {
            if (null == strStartDatetimeCustom || "".equals(strStartDatetimeCustom)) {
                Toast.makeText(this, "请输入开始时间", Toast.LENGTH_SHORT).show();
            } else if (null == strEndDatetimeCustom || "".equals(strEndDatetimeCustom)) {
                Toast.makeText(this, "请输入结束时间", Toast.LENGTH_SHORT).show();
            } else if (strStartDatetimeCustom.compareTo(strEndDatetimeCustom) > 0) {
                Toast.makeText(this, "开始时间不能晚于结束时间...", Toast.LENGTH_LONG).show();
            } else {
                strStartDatetimeCustom = strStartDatetimeCustom + " 00:00";
                strEndDatetimeCustom = strEndDatetimeCustom + " 23:59";
                intent.putExtra(EXTRA_START_DATETIME, strStartDatetimeCustom);
                intent.putExtra(EXTRA_END_DATETIME, strEndDatetimeCustom);
                startActivity(intent);
            }

        } else {
            intent.putExtra(EXTRA_START_DATETIME, strStartDatetimePrepare);
            intent.putExtra(EXTRA_END_DATETIME, strEndDatetimePrepare);
            startActivity(intent);
        }
    }

    private void changeDatetimeChooseType() {
        if (isDatetimeCustom) {
            datetimeGridView.setVisibility(View.GONE);
            datetimeCustomLayout.setVisibility(View.VISIBLE);
            tvDatetimeChoose.setText(R.string.search_datetime_prepare);
            isDatetimeCustom = false;
        } else {
            datetimeGridView.setVisibility(View.VISIBLE);
            datetimeCustomLayout.setVisibility(View.GONE);
            tvDatetimeChoose.setText(R.string.search_datetime_custom);
            isDatetimeCustom = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
