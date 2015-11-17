package com.wangsy.ouraccounts.model;

import com.wangsy.ouraccounts.R;

import java.io.Serializable;

/**
 * 类型图标相关内容
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class IconModel implements Serializable {
    private int id;
    public boolean isOut;
    public String type;
    public int typeTextColor;
    public String normalIcon;
    public String selectedIcon;
    public String iconImageToShow;
    private int counts;

    public IconModel(int id, boolean isOut, String type, String normalIcon, String selectedIcon, int counts) {
        this.id = id;
        this.isOut = isOut;
        this.type = type;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
        this.typeTextColor = R.color.color_icon_normal;
        this.iconImageToShow = normalIcon;
        this.counts = counts;
    }

    public int getId() {
        return id;
    }

    public int getCounts() {
        return counts;
    }

    @Override
    public String toString() {
        return "IconModel{" +
                "isOut=" + isOut +
                ", type='" + type + '\'' +
                ", typeTextColor=" + typeTextColor +
                ", normalIcon=" + normalIcon +
                ", selectedIcon=" + selectedIcon +
                ", iconImageToShow=" + iconImageToShow +
                '}';
    }
}
