package com.lr3_428_04.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RoomItem implements Parcelable {
    private int id;
    private String roomNumber;
    private String roomType;
    private String equipType;
    private String equipBrand;
    private int roomTypeId;
    private int equipTypeId;
    public static final Creator<RoomItem> CREATOR = new Creator<RoomItem>() {
        @Override
        public RoomItem createFromParcel(Parcel source) {
            int id = source.readInt();
            String roomNumber = source.readString();
            String roomType = source.readString();
            String equipType = source.readString();
            String equipBrand = source.readString();
            int roomTypeId = source.readInt();
            int equipTypeId = source.readInt();
            return new RoomItem(id, roomNumber, roomType, equipType, equipBrand, roomTypeId, equipTypeId);
        }

        @Override
        public RoomItem[] newArray(int size) {
            return new RoomItem[size];
        }
    };

    public RoomItem(int id, String roomNumber, String roomType,
                    String equipType, String equipBrand, int roomTypeId, int equipTypeId) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.equipType = equipType;
        this.equipBrand = equipBrand;
        this.roomTypeId = roomTypeId;
        this.equipTypeId = equipTypeId;
    }

    public int getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public String getEquipType() { return equipType; }
    public String getEquipBrand() { return equipBrand; }
    public int getRoomTypeId() { return roomTypeId; }

    public int getEquipTypeId() { return equipTypeId; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(roomNumber);
        dest.writeString(roomType);
        dest.writeString(equipType);
        dest.writeString(equipBrand);
        dest.writeInt(roomTypeId);
        dest.writeInt(equipTypeId);
    }
}
