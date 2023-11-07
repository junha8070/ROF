package com.xlntsmmr.xlnt_timeline.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "category")
public class CategoryEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uuid")
    String uuid; // UUID 열 추가

    @ColumnInfo(name = "color")
    String color;

    @ColumnInfo(name = "title")
    String title;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CategoryEntity(String uuid, String color, String title) {
        this.uuid = uuid;
        this.color = color;
        this.title = title;
    }
}
