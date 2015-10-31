package com.wangsy.ouraccounts.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.adapter.SearchConditionAdapter;
import com.wangsy.ouraccounts.model.DateTimeConstants;
import com.wangsy.ouraccounts.model.SearchTextModel;
import com.wangsy.ouraccounts.model.TypeConstants;

import java.util.List;

/**
 * 搜索条件选择界面
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class SearchConditionActivity extends Activity {

    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_START_DATETIME = "extra_start_datetime";
    public static final String EXTRA_END_DATETIME = "extra_end_datetime";

    public static final String EXTRA_SEARCH_FLAG = "extra_search_flag";
    public static final int SEARCH_FLAG_ALL = 0;
    public static final int SEARCH_FLAG_DATETIME = 1;

    private int searchFlag;

    private GridView datetimeGridView;
    private GridView typeGridView;

    private SearchConditionAdapter datetimeAdapter;
    private SearchConditionAdapter typeAdapter;

    private List<SearchTextModel> datetimeList;
    private List<SearchTextModel> typeList;

    private String strType;
    private String strDatetime;
    private String strStartDatetime;
    private String strEndDatetime;

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
        // 时间列表
        initDatetimeGridView();
        // 类型列表
        initTypeGridView();
    }

    private void initTypeGridView() {
        typeGridView = (GridView) findViewById(R.id.id_search_type);
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
        datetimeList = DateTimeConstants.getDatetimeList();
        datetimeGridView = (GridView) findViewById(R.id.id_search_datetime);
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
            }
        });
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
        imgBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchConditionActivity.this, SearchResultActivity.class);
                intent.putExtra(EXTRA_TYPE, strType);
                intent.putExtra(EXTRA_START_DATETIME, strStartDatetime);
                intent.putExtra(EXTRA_END_DATETIME, strEndDatetime);
                startActivity(intent);
            }
        });
    }

    private void initButtonLeft() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.id_title_left_btn);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setImageResource(R.mipmap.icon_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
