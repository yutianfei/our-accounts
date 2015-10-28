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

        IconModel icon = new IconModel(false, TypeConstants.INCOME, R.string.icon_income, R.mipmap.normal_income, R.mipmap.selected_income);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.string.icon_food, R.mipmap.normal_food, R.mipmap.selected_food);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.CLOTHES, R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.CAR, R.string.icon_car, R.mipmap.normal_car, R.mipmap.selected_car);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.TRAFFIC, R.string.icon_traffic, R.mipmap.normal_traffic, R.mipmap.selected_traffic);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.MOBILE, R.string.icon_mobile, R.mipmap.normal_mobile, R.mipmap.selected_mobile);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.HAIR_CUT, R.string.icon_hair_cut, R.mipmap.normal_hair_cut, R.mipmap.selected_hair_cut);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.APPS, R.string.icon_apps, R.mipmap.normal_apps, R.mipmap.selected_apps);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.HOTEL, R.string.icon_hotel, R.mipmap.normal_hotel, R.mipmap.selected_hotel);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.LEISURE, R.string.icon_leisure, R.mipmap.normal_leisure, R.mipmap.selected_leisure);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.SHOPPING, R.string.icon_shopping, R.mipmap.normal_shopping, R.mipmap.selected_shopping);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.FILM, R.string.icon_film, R.mipmap.normal_film, R.mipmap.selected_film);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.KIDS, R.string.icon_kids, R.mipmap.normal_kids, R.mipmap.selected_kids);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.PET, R.string.icon_pet, R.mipmap.normal_pet, R.mipmap.selected_pet);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.MEDICINE, R.string.icon_medicine, R.mipmap.normal_medicine, R.mipmap.selected_medicine);
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.GENERAL, R.string.icon_general, R.mipmap.normal_general, R.mipmap.selected_general);
        iconsList.add(icon);

        return iconsList;
    }

}
