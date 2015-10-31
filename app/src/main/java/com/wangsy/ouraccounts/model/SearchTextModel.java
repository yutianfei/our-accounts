package com.wangsy.ouraccounts.model;

import com.wangsy.ouraccounts.R;

/**
 * 搜索条件字体model
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class SearchTextModel {
    public String text;
    public int textColor;
    public int backgroundResourceId;

    public SearchTextModel(String text) {
        this.text = text;
        this.textColor = R.color.color_text_normal;
        this.backgroundResourceId = R.drawable.bg_search_text_normal;
    }
}
