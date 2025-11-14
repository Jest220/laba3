package com.lr3_428_04.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr3_428_04.model.RoomItem;

import java.util.ArrayList;
import java.util.List;

public class RoomDao {
    private SQLiteDatabase db;

    public RoomDao(SQLiteDatabase db) {
        this.db = db;
    }

    public List<RoomItem> getAll() {
        List<RoomItem> rooms = new ArrayList<>();
        String sql = "SELECT r.id, r.room_number, rt.type AS room_type, et.type AS equip_type, r.equip_brand, r.room_type_id, r.equip_type_id\n" +
                "FROM rooms r\n" +
                "JOIN room_types rt ON r.room_type_id = rt.id\n" +
                "JOIN equip_types et ON r.equip_type_id = et.id;";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String roomNumber = cursor.getString(1);
                String roomType = cursor.getString(2);
                String equipType = cursor.getString(3);
                String equipBrand = cursor.getString(4);
                int roomTypeId = cursor.getInt(5);
                int equipTypeId = cursor.getInt(6);
                RoomItem room = new RoomItem(id, roomNumber, roomType,
                        equipType, equipBrand, roomTypeId, equipTypeId);
                rooms.add(room);
            }
        }
        return rooms;
    }

    public long insert(String roomNumber, int roomTypeId, int equipTypeId, String equipBrand) {
        ContentValues v = new ContentValues();
        v.put("room_number", roomNumber);
        v.put("room_type_id", roomTypeId);
        v.put("equip_type_id", equipTypeId);
        v.put("equip_brand", equipBrand);
        return db.insert("rooms", null, v);
    }

    public void update(int id, String roomNumber, int roomTypeId, int equipTypeId, String equipBrand) {
        ContentValues v = new ContentValues();
        v.put("room_number", roomNumber);
        v.put("room_type_id", roomTypeId);
        v.put("equip_type_id", equipTypeId);
        v.put("equip_brand", equipBrand);
        db.update("rooms", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void delete(int id) {
        db.delete("rooms", "id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteAll() {
        return db.delete("rooms", null, null);
    }
}
