package com.wangsy.ouraccounts.callback;

/**
 * 当icon被选中时的回调处理
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public interface IconSelectedCallback {
    void onIconSelected(boolean isOut, String type, String iconName);
}
