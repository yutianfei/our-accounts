package com.wangsy.ouraccounts.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 便捷搜索的时间选择
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class DateTimeConstants {
    public static final String ALL = "全部";
    public static final String ONE_MONTH = "一个月";
    public static final String TWO_MONTH = "两个月";
    public static final String THREE_MONTH = "三个月";

    public static List<SearchTextModel> getDatetimeList() {
        List<SearchTextModel> list = new ArrayList<>();

        SearchTextModel model = new SearchTextModel(ONE_MONTH);
        list.add(model);

        model = new SearchTextModel(TWO_MONTH);
        list.add(model);

        model = new SearchTextModel(THREE_MONTH);
        list.add(model);

        model = new SearchTextModel(ALL);
        list.add(model);

        return list;
    }
}
