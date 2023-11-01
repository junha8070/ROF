package com.xlntsmmr.xlnt_timeline.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.xlntsmmr.xlnt_timeline.Dao.CategoryDao;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.DB.AppDatabase;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
    }

    public void insertCategory(CategoryEntity category) {
        new InsertCategoryAsyncTask(categoryDao).execute(category);
    }

    private static class InsertCategoryAsyncTask extends AsyncTask<CategoryEntity, Void, Void> {

        private CategoryDao categoryDao;

        InsertCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(CategoryEntity... categories) {
            categoryDao.insertCategory(categories[0]);
            return null;
        }
    }

    public LiveData<List<CategoryEntity>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public void deleteCategory(String categoryUUID) {
        new DeleteCategoryAsyncTask(categoryDao).execute(categoryUUID);
    }

    public void editCategory(String categoryUUID, String newTitle) {
        new EditCategoryAsyncTask(categoryDao).execute(categoryUUID, newTitle);
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<String, Void, Void> {

        private CategoryDao categoryDao;

        DeleteCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(String... categoryUUIDs) {
            categoryDao.deleteCategory(categoryUUIDs[0]);
            return null;
        }
    }

    private static class EditCategoryAsyncTask extends AsyncTask<String, Void, Void> {

        private CategoryDao categoryDao;

        EditCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(String... params) {
            String categoryUUID = params[0];
            String newTitle = params[1];
            categoryDao.editCategory(categoryUUID, newTitle);
            return null;
        }
    }
}

