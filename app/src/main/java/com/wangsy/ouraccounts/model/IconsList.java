package com.wangsy.ouraccounts.model;

import com.wangsy.ouraccounts.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangsy on 15/10/21.
 */
public class IconsList {

    /**
     * 获取所有的icon数据
     */
    public static List<IconModel> getIconsList() {
        List<IconModel> iconsList = new ArrayList<>();

        IconModel icon = new IconModel(TypeConstants.Food, R.string.icon_food, R.mipmap.normal_food, R.mipmap.selected_food);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_car, R.mipmap.normal_car, R.mipmap.selected_car);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_apps, R.mipmap.normal_apps, R.mipmap.selected_apps);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_hair_cut, R.mipmap.normal_hair_cut, R.mipmap.selected_hair_cut);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_hotel, R.mipmap.normal_hotel, R.mipmap.selected_hotel);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_leisure, R.mipmap.normal_leisure, R.mipmap.selected_leisure);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_shopping, R.mipmap.normal_shopping, R.mipmap.selected_shopping);
        iconsList.add(icon);

        icon = new IconModel(false, TypeConstants.Food, R.string.icon_income, R.mipmap.normal_income, R.mipmap.selected_income);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_car, R.mipmap.normal_car, R.mipmap.selected_car);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_apps, R.mipmap.normal_apps, R.mipmap.selected_apps);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_hair_cut, R.mipmap.normal_hair_cut, R.mipmap.selected_hair_cut);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_shopping, R.mipmap.normal_shopping, R.mipmap.selected_shopping);
        iconsList.add(icon);

        icon = new IconModel(false, TypeConstants.Food, R.string.icon_income, R.mipmap.normal_income, R.mipmap.selected_income);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_car, R.mipmap.normal_car, R.mipmap.selected_car);
        iconsList.add(icon);

        return iconsList;
    }

}
