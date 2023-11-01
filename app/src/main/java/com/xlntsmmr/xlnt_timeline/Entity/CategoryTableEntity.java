package com.xlntsmmr.xlnt_timeline.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class CategoryTableEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="timeline_uuid")
    private String timeline_uuid;

    @ColumnInfo(name = "year")
    int year;
    @ColumnInfo(name = "month")
    int month;
    @ColumnInfo(name = "day")
    int day;

    public String getTimeline_uuid() {
        return timeline_uuid;
    }

    public void setTimeline_uuid(String timeline_uuid) {
        this.timeline_uuid = timeline_uuid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
