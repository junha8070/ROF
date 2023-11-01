package com.xlntsmmr.xlnt_timeline.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xlntsmmr.xlnt_timeline.Entity.CategoryTableEntity;

import java.util.List;

@Dao
public interface CategoryTableDao {

    @Insert
    void insertCategoryTable(CategoryTableEntity categoryTableEntity);

    @Query("SELECT * FROM category_table")
    LiveData<List<CategoryTableEntity>> getAllCategoryTable();

}
