package com.wangsy.ouraccounts.model;

import com.wangsy.ouraccounts.R;

import java.io.Serializable;

/**
 * Created by wangsy on 15/10/21.
 */
public class IconModel implements Serializable {

    public boolean isOut;
    public String type;
    public int typeTextColor;
    public int normalIcon;
    public int selectedIcon;
    public int iconImageToShow;

    public IconModel(boolean isOut, String type, int normalIcon, int selectedIcon) {
        this.isOut = isOut;
        this.type = type;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
        this.typeTextColor = R.color.color_icon_normal;
        this.iconImageToShow = normalIcon;
    }

    /***
     * 默认为支出
     */
    public IconModel(String type, int normalIcon, int selectedIcon) {
        this(true, type, normalIcon, selectedIcon);
    }
}
