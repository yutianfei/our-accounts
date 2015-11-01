package com.wangsy.ouraccounts.callback;

import java.util.Map;

/**
 * 接收查询到的数据之后的处理
 * <p/>
 * Created by wangsy on 15/11/1.
 */
public interface OnQueryDataReceived {
    void onQueryDataReceived(Map<String, Object> result);
}
