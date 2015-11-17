package com.wangsy.ouraccounts.constants;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wangsy.ouraccounts.model.IconModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置/获取类型icon数据
 * <p/>
 * Created by wangsy on 15/10/21.
 */
public class IconConstants {

    private static SQLiteDatabase db;

    /**
     * 获取所有的icon数据
     */
    public static List<IconModel> getIconsList() {

        List<IconModel> iconsList = new ArrayList<>();
        SQLiteDatabase database = getTypeDatabase();
        Cursor cursor = database.query("account_type", null, null, null, null, null, "counts desc");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                boolean isOut = cursor.getInt(cursor.getColumnIndex("isout")) != 0;
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String normalIcon = cursor.getString(cursor.getColumnIndex("normalicon"));
                String selectedIcon = cursor.getString(cursor.getColumnIndex("selectedicon"));
                int counts = cursor.getInt(cursor.getColumnIndex("counts"));

                IconModel icon = new IconModel(id, isOut, type, normalIcon, selectedIcon, counts);
                iconsList.add(icon);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return iconsList;
    }

    /**
     * 取得数据库连接
     */
    public static SQLiteDatabase getTypeDatabase() {
        if (db == null) {
            db = SQLiteDatabase.openOrCreateDatabase("data/data/com.wangsy.ouraccounts/databases/type_database.db", null);
        }
        return db;
    }

    /**
     * 更新数据库中类型信息
     */
    public static void updateTypeCountsInDatabase(int id, int counts) {
        SQLiteDatabase db = IconConstants.getTypeDatabase();
        ContentValues values = new ContentValues();
        values.put("counts", counts);
        int result = db.update("account_type", values, "id = ?", new String[]{id + ""});
        Log.i("TypeDatabase", "icon counts added : " + result);
    }
}
