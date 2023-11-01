package com.xlntsmmr.xlnt_timeline.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insertCategory(CategoryEntity category);

    @Query("SELECT * FROM categories")
    LiveData<List<CategoryEntity>> getAllCategories();

    @Query("DELETE FROM categories WHERE uuid = :categoryUUID")
    void deleteCategory(String categoryUUID);

    @Query("UPDATE categories SET title = :newTitle WHERE uuid = :categoryUUID")
    void editCategory(String categoryUUID, String newTitle);
}
