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

    @Query("SELECT * FROM category")
    LiveData<List<CategoryEntity>> getAllCategories();

    @Query("DELETE FROM category WHERE uuid = :categoryUUID")
    void deleteCategory(String categoryUUID);

    @Query("UPDATE category SET title = :newTitle WHERE uuid = :categoryUUID")
    void editCategory(String categoryUUID, String newTitle);

    @Query("SELECT COUNT(*) FROM category")
    LiveData<Integer> getCategoryCount();

    @Query("UPDATE category SET position = :newPosition WHERE uuid = :categoryUUID")
    void updateCategoryPosition(String categoryUUID, int newPosition);

    @Query("UPDATE category SET position = position - 1 WHERE position > :currentPosition")
    void decrementCategoryPositions(int currentPosition);

}
