package com.lr3_428_04.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr3_428_04.model.SimpleTypeModel;

import java.util.ArrayList;
import java.util.List;

public class SimpleTypeDao {
    private SimpleTypeTableName tableName;
    private SQLiteDatabase db;

    public SimpleTypeDao(SimpleTypeTableName tableName, SQLiteDatabase db) {
        this.tableName = tableName;
        this.db = db;
    }

    public List<SimpleTypeModel> getAll() {
        List<SimpleTypeModel> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName.getName();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                list.add(new SimpleTypeModel(id, type));
            }
        }
        return list;
    }

    public long insert(String type) {
        ContentValues v  = new ContentValues();
        v.put("type", type);
        return db.insert(tableName.getName(), null, v);
    }

    public void update(int id, String type) {
        ContentValues v  = new ContentValues();
        v.put("type", type);
        db.update(tableName.getName(), v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void delete(int id) {
        db.delete(tableName.getName(), "id = ?", new String[]{String.valueOf(id)});
    }

}
