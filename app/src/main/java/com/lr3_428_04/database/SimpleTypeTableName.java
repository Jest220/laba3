package com.lr3_428_04.database;

import androidx.annotation.NonNull;

public enum SimpleTypeTableName {
    ROOM_TYPES("room_types", "Типы помещений"),
    EQUIP_TYPES("equip_types", "Типы оборудований");

    private String name;
    private String readableName;

    SimpleTypeTableName(String name, String readableName) {
        this.name = name;
        this.readableName = readableName;
    }

    public String getName() { return name; }
    public String getReadableName() { return readableName; }


    @NonNull
    @Override
    public String toString() {
        return getReadableName();
    }
}
