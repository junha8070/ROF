package com.xlntsmmr.xlnt_timeline.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xlntsmmr.xlnt_timeline.Entity.SettingEntity;

@Dao
public interface SettingDao {

    @Insert
    void insertSetting(SettingEntity setting);

    @Query("UPDATE settings SET date_skip = :newDateSkipValue, alarm = :newAlarmValue WHERE id = :id")
    void updateSetting(boolean newDateSkipValue, boolean newAlarmValue, int id);

    @Query("SELECT * FROM settings")
    LiveData<SettingEntity> getAllSettings();

}
