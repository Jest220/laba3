package com.lr3_428_04.model;

import androidx.annotation.NonNull;

public class SimpleTypeModel {
    private int id;
    private String type;

    public SimpleTypeModel(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() { return id; }
    public String getType() { return type; }

    @NonNull
    @Override
    public String toString() {
        return type;
    }
}
