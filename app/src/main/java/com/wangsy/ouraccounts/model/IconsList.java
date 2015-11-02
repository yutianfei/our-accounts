package com.wangsy.ouraccounts.model;

import com.wangsy.ouraccounts.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置/获取类型icon数据
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class IconsList {

    /**
     * 获取所有的icon数据
     */
    public static List<IconModel> getIconsList() {
        List<IconModel> iconsList = new ArrayList<>();

        IconModel icon = new IconModel(false, TypeConstants.INCOME, R.mipmap.normal_income, R.mipmap.selected_income, "selected_income");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.Food, R.mipmap.normal_food, R.mipmap.selected_food, "selected_food");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.CLOTHES, R.mipmap.normal_clothes, R.mipmap.selected_clothes, "selected_clothes");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.CAR, R.mipmap.normal_car, R.mipmap.selected_car, "selected_car");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.HOME, R.mipmap.normal_home, R.mipmap.selected_home, "selected_home");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.TRAFFIC, R.mipmap.normal_traffic, R.mipmap.selected_traffic, "selected_traffic");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.MOBILE, R.mipmap.normal_mobile, R.mipmap.selected_mobile, "selected_mobile");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.HAIR_CUT, R.mipmap.normal_hair_cut, R.mipmap.selected_hair_cut, "selected_hair_cut");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.APPS, R.mipmap.normal_apps, R.mipmap.selected_apps, "selected_apps");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.HOTEL, R.mipmap.normal_hotel, R.mipmap.selected_hotel, "selected_hotel");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.LEISURE, R.mipmap.normal_leisure, R.mipmap.selected_leisure, "selected_leisure");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.SHOPPING, R.mipmap.normal_shopping, R.mipmap.selected_shopping, "selected_shopping");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.FILM, R.mipmap.normal_film, R.mipmap.selected_film, "selected_film");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.KIDS, R.mipmap.normal_kids, R.mipmap.selected_kids, "selected_kids");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.PET, R.mipmap.normal_pet, R.mipmap.selected_pet, "selected_pet");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.MEDICINE, R.mipmap.normal_medicine, R.mipmap.selected_medicine, "selected_medicine");
        iconsList.add(icon);

        icon = new IconModel(TypeConstants.GENERAL, R.mipmap.normal_general, R.mipmap.selected_general, "selected_general");
        iconsList.add(icon);

        return iconsList;
    }

}
