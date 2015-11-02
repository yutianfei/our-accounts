package com.wangsy.ouraccounts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wangsy.ouraccounts.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 显示数据列表
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class AccountListActivity extends BaseListActivity {
    public static final String EXTRA_TYPE_NAME = "extra_type_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        strType = getIntent().getStringExtra(EXTRA_TYPE_NAME);
        super.onCreate(savedInstanceState);

        TextView title = (TextView) findViewById(R.id.id_title);
        title.setText(strType);

        // 显示右侧按钮：搜索
        initButtonRight();
    }

    private void initButtonRight() {
        ImageButton imgBtnRight = (ImageButton) findViewById(R.id.id_title_right_btn);
        imgBtnRight.setVisibility(View.VISIBLE);
        imgBtnRight.setImageResource(R.mipmap.icon_search);
        imgBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseListActivity.isDataModified = false;
                Intent intent = new Intent(AccountListActivity.this, SearchConditionActivity.class);
                intent.putExtra(SearchConditionActivity.EXTRA_TYPE, strType);
                intent.putExtra(SearchConditionActivity.EXTRA_SEARCH_FLAG, SearchConditionActivity.SEARCH_FLAG_DATETIME);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
