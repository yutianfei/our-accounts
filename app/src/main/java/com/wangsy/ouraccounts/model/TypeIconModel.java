package com.wangsy.ouraccounts.model;

import com.wangsy.ouraccounts.R;

import java.io.Serializable;

/**
 * Created by wangsy on 15/10/21.
 */
public class TypeIconModel implements Serializable {

    public boolean isOut;
    public String type;

    public int iconName;
    public int iconNameColor;
    public int normalIcon;
    public int selectedIcon;
    public int iconImageToShow;

    public TypeIconModel() {
    }

    public TypeIconModel(boolean isOut, String type, int iconName, int normalIcon, int selectedIcon) {
        this.isOut = isOut;
        this.type = type;

        this.iconName = iconName;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;

        this.iconNameColor = R.color.color_icon_normal;
        this.iconImageToShow = normalIcon;
    }

    /***
     * 默认为支出
     */
    public TypeIconModel(String type, int iconName, int normalIcon, int selectedIcon) {
        this(true, type, iconName, normalIcon, selectedIcon);
    }
}
