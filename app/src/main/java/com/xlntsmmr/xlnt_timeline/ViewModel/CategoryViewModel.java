package com.xlntsmmr.xlnt_timeline.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository repository;


    private LiveData<List<CategoryEntity>> allCategories;


    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
        allCategories = repository.getAllCategories();
    }

    public LiveData<List<CategoryEntity>> getAllCategories() {
        return allCategories;
    }

    public void insertCategory(CategoryEntity category) {
        repository.insertCategory(category);
    }

    public void deleteCategory(String categoryUUID) {
        repository.deleteCategory(categoryUUID);
    }

    public void editCategory(String categoryUUID, String newTitle) {
        repository.editCategory(categoryUUID, newTitle);
    }
}
