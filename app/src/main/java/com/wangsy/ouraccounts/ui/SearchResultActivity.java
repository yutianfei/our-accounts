package com.wangsy.ouraccounts.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 显示搜索结果
 * <p/>
 * Created by wangsy on 14/10/31.
 */
public class SearchResultActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        strType = getIntent().getStringExtra(SearchConditionActivity.EXTRA_TYPE);
        strStartDatetime = getIntent().getStringExtra(SearchConditionActivity.EXTRA_START_DATETIME);
        strEndDatetime = getIntent().getStringExtra(SearchConditionActivity.EXTRA_END_DATETIME);

        super.onCreate(savedInstanceState);

        BaseListActivity.isDataModified = true;

        TextView title = (TextView) findViewById(R.id.id_title);
        title.setText(R.string.title_search_result);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
