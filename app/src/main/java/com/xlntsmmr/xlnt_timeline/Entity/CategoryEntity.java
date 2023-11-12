package com.xlntsmmr.xlnt_timeline.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "category")
public class CategoryEntity implements Parcelable{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uuid")
    String uuid; // UUID 열 추가

    @ColumnInfo(name = "color")
    String color;

    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "position")
    int position;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CategoryEntity(String uuid, String color, String title, int position) {
        this.uuid = uuid;
        this.color = color;
        this.title = title;
        this.position = position;
    }

    protected CategoryEntity(Parcel in) {
        uuid = in.readString();
        color = in.readString();
        title = in.readString();
        position = in.readInt();
    }

    public static final Parcelable.Creator<CategoryEntity> CREATOR = new Parcelable.Creator<CategoryEntity>() {
        @Override
        public CategoryEntity createFromParcel(Parcel in) {
            return new CategoryEntity(in);
        }

        @Override
        public CategoryEntity[] newArray(int size) {
            return new CategoryEntity[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(color);
        dest.writeString(title);
        dest.writeInt(position);
    }
}
