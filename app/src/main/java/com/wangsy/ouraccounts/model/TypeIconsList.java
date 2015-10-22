package com.wangsy.ouraccounts.model;

import com.wangsy.ouraccounts.R;
import com.wangsy.ouraccounts.model.TypeConstants;
import com.wangsy.ouraccounts.model.TypeIconModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangsy on 15/10/21.
 */
public class TypeIconsList {

    public static List<TypeIconModel> getIconsList() {
        List<TypeIconModel> iconsList = new ArrayList<>();

        TypeIconModel icon = new TypeIconModel(TypeConstants.Food, R.string.icon_food, R.mipmap.normal_food, R.mipmap.selected_food);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_car, R.mipmap.normal_car, R.mipmap.selected_car);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_apps, R.mipmap.normal_apps, R.mipmap.selected_apps);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_hair_cut, R.mipmap.normal_hair_cut, R.mipmap.selected_hair_cut);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_hotel, R.mipmap.normal_hotel, R.mipmap.selected_hotel);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_leisure, R.mipmap.normal_leisure, R.mipmap.selected_leisure);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_shopping, R.mipmap.normal_shopping, R.mipmap.selected_shopping);
        iconsList.add(icon);

        icon = new TypeIconModel(false, TypeConstants.Food, R.string.icon_income, R.mipmap.normal_income, R.mipmap.selected_income);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_car, R.mipmap.normal_car, R.mipmap.selected_car);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_apps, R.mipmap.normal_apps, R.mipmap.selected_apps);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_hair_cut, R.mipmap.normal_hair_cut, R.mipmap.selected_hair_cut);
        iconsList.add(icon);

        icon = new TypeIconModel(TypeConstants.Food,R.string.icon_clothes, R.mipmap.normal_clothes, R.mipmap.selected_clothes);
        iconsList.add(icon);

        return iconsList;
    }

}
