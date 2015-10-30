package com.wangsy.ouraccounts.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 账目数据模型（数据库存储）
 * <p/>
 * 通过继承DataSupport，使用LitePal进行数据库操作
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class AccountModel extends DataSupport implements Serializable {
    private long id;
    private boolean isOut = true;
    private String type = "";
    private float amount = 0;
    private String comment = "";
    private String datetime = "";
    private String iconImageName;

    public long getId() {
        return getBaseObjId();
    }

    public String getIconImageName() {
        return iconImageName;
    }

    public void setIconImageName(String iconImageName) {
        this.iconImageName = iconImageName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean isOut) {
        this.isOut = isOut;
    }

    @Override
    public String toString() {
        return "isOut = " + isOut + ",type = " + type + ",amount = " + amount
                + ",datetime = " + datetime + ",comment = " + comment + ",icon = " + iconImageName;
    }
}
