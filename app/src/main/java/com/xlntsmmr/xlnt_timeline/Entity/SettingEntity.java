package com.xlntsmmr.xlnt_timeline.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class SettingEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="date_skip")
    boolean date_skip;
    @ColumnInfo(name="alarm")
    boolean alarm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDate_skip() {
        return date_skip;
    }

    public void setDate_skip(boolean date_skip) {
        this.date_skip = date_skip;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
