package com.xlntsmmr.xlnt_timeline.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;

import java.util.List;

@Dao
public interface TimeLineDao {

    @Insert
    void insertTimeLine(TimeLineEntity timeLine);

    @Query("SELECT * FROM timeline")
    LiveData<List<TimeLineEntity>> getAllTimeLine();

    @Query("DELETE FROM timeline WHERE uuid = :uuid")
    void deleteTimeLineByUUID(String uuid);

    @Query("UPDATE timeline SET status = :newStatus WHERE uuid = :uuid")
    void updateStatusByUUID(String uuid, int newStatus);

    @Query("SELECT * FROM timeline WHERE uuid = :uuid")
    LiveData<TimeLineEntity> getTimeLineByUUID(String uuid);

    @Update
    void updateTimeLine(TimeLineEntity timeLine);

    @Query("UPDATE timeline SET category = :newCategory WHERE category_uuid = :categoryUUID")
    void updateCategoryByCategoryUUID(String categoryUUID, String newCategory);

    @Query("DELETE FROM timeline WHERE category_uuid = :categoryUUID")
    void deleteTimelinesByCategoryUUID(String categoryUUID);

}
