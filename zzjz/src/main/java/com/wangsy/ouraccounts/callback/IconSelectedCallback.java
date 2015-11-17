package com.wangsy.ouraccounts.callback;

import com.wangsy.ouraccounts.model.IconModel;

/**
 * 当icon被选中时的回调处理
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public interface IconSelectedCallback {
    void onIconSelected(IconModel iconModel);
}
