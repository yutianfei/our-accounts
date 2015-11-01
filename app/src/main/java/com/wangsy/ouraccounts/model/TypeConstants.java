package com.wangsy.ouraccounts.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费类型
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class TypeConstants {
    public static final String ALL = "全部";
    public static final String Food = "美食";
    public static final String APPS = "apps";
    public static final String CAR = "爱车";
    public static final String CLOTHES = "衣物";
    public static final String FILM = "电影";
    public static final String GENERAL = "其他";
    public static final String HAIR_CUT = "理发";
    public static final String HOTEL = "酒店";
    public static final String INCOME = "收入";
    public static final String KIDS = "儿童";
    public static final String LEISURE = "休闲";
    public static final String MEDICINE = "医药";
    public static final String MOBILE = "手机";
    public static final String PET = "宠物";
    public static final String SHOPPING = "购物";
    public static final String TRAFFIC = "交通";
    public static final String HOME = "家";

    public static List<SearchTextModel> getTypeList() {
        List<SearchTextModel> list = new ArrayList<>();

        SearchTextModel model = new SearchTextModel(INCOME);
        list.add(model);

        model = new SearchTextModel(Food);
        list.add(model);

        model = new SearchTextModel(CLOTHES);
        list.add(model);

        model = new SearchTextModel(CAR);
        list.add(model);

        model = new SearchTextModel(HOME);
        list.add(model);

        model = new SearchTextModel(TRAFFIC);
        list.add(model);

        model = new SearchTextModel(MOBILE);
        list.add(model);

        model = new SearchTextModel(HAIR_CUT);
        list.add(model);

        model = new SearchTextModel(APPS);
        list.add(model);

        model = new SearchTextModel(HOTEL);
        list.add(model);

        model = new SearchTextModel(LEISURE);
        list.add(model);

        model = new SearchTextModel(SHOPPING);
        list.add(model);

        model = new SearchTextModel(FILM);
        list.add(model);

        model = new SearchTextModel(KIDS);
        list.add(model);

        model = new SearchTextModel(PET);
        list.add(model);

        model = new SearchTextModel(MEDICINE);
        list.add(model);

        model = new SearchTextModel(GENERAL);
        list.add(model);

        model = new SearchTextModel(ALL);
        list.add(model);

        return list;
    }
}
