package com.wangsy.ouraccounts.model;

/**
 * 版本信息
 * <p/>
 * Created by wangsy on 15/11/4.
 */
public class VersionModel {
    public int versionCode;
    public String versionName;
    public String downloadUrl;

    @Override
    public String toString() {
        return "VersionModel{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
