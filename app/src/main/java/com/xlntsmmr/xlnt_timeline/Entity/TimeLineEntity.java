package com.xlntsmmr.xlnt_timeline.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "timeline")
public class TimeLineEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uuid")
    String uuid;
    @ColumnInfo(name = "category_uuid")
    String category_uuid;

    @ColumnInfo(name = "contents")
    String contents;

    @ColumnInfo(name = "memo")
    String memo;

    //TODO: status getter setter 세팅
    @ColumnInfo(name = "status")
    int status;

    @ColumnInfo(name = "year")
    int year;
    @ColumnInfo(name = "month")
    int month;
    @ColumnInfo(name = "day")
    int day;

    @ColumnInfo(name = "add_year")
    int add_year;
    @ColumnInfo(name = "add_month")
    int add_month;
    @ColumnInfo(name = "add_day")
    int add_day;
    @ColumnInfo(name = "add_hour")
    int add_hour;
    @ColumnInfo(name = "add_minute")
    int add_minute;
    @ColumnInfo(name = "add_second")
    int add_second;

    @ColumnInfo(name = "category")
    String category;

    @ColumnInfo(name = "importance")
    int importance;

    @ColumnInfo(name = "alarm")
    boolean alarm;
    @ColumnInfo(name = "alarm_hour")
    int alarm_hour;
    @ColumnInfo(name = "alarm_minute")
    int alarm_minute;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCategory_uuid() {
        return category_uuid;
    }

    public void setCategory_uuid(String category_uuid) {
        this.category_uuid = category_uuid;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getAdd_year() {
        return add_year;
    }

    public void setAdd_year(int add_year) {
        this.add_year = add_year;
    }

    public int getAdd_month() {
        return add_month;
    }

    public void setAdd_month(int add_month) {
        this.add_month = add_month;
    }

    public int getAdd_day() {
        return add_day;
    }

    public void setAdd_day(int add_day) {
        this.add_day = add_day;
    }

    public int getAdd_hour() {
        return add_hour;
    }

    public void setAdd_hour(int add_hour) {
        this.add_hour = add_hour;
    }

    public int getAdd_minute() {
        return add_minute;
    }

    public void setAdd_minute(int add_minute) {
        this.add_minute = add_minute;
    }

    public int getAdd_second() {
        return add_second;
    }

    public void setAdd_second(int add_second) {
        this.add_second = add_second;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public int getAlarm_hour() {
        return alarm_hour;
    }

    public void setAlarm_hour(int alarm_hour) {
        this.alarm_hour = alarm_hour;
    }

    public int getAlarm_minute() {
        return alarm_minute;
    }

    public void setAlarm_minute(int alarm_minute) {
        this.alarm_minute = alarm_minute;
    }

    public TimeLineEntity(String uuid, String category_uuid, String category, String contents, String memo, int status, int year, int month, int day, int add_year, int add_month, int add_day, int add_hour, int add_minute, int add_second, int importance, boolean alarm, int alarm_hour, int alarm_minute) {
        this.uuid = uuid;
        this.category_uuid = category_uuid;
        this.category = category;
        this.contents = contents;
        this.memo = memo;
        this.status = status;
        this.year = year;
        this.month = month;
        this.day = day;
        this.add_year = add_year;
        this.add_month = add_month;
        this.add_day = add_day;
        this.add_hour = add_hour;
        this.add_minute = add_minute;
        this.add_second = add_second;
        this.importance = importance;
        this.alarm = alarm;
        this.alarm_hour = alarm_hour;
        this.alarm_minute = alarm_minute;
    }
}
