package com.xlntsmmr.xlnt_timeline.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.xlntsmmr.xlnt_timeline.Dao.CategoryTableDao;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryTableEntity;
import com.xlntsmmr.xlnt_timeline.DB.AppDatabase;

import java.util.List;

public class CategoryTableRepository {

    private CategoryTableDao categoryTableDao;

    public CategoryTableRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        categoryTableDao = db.categoryTableDao();
    }

    public void insertCategoryTable(CategoryTableEntity categoryTable) {
        new CategoryTableRepository.InsertCategoryTableAsyncTask(categoryTableDao).execute(categoryTable);
    }

    private static class InsertCategoryTableAsyncTask extends AsyncTask<CategoryTableEntity, Void, Void> {

        private CategoryTableDao categoryTableDao;

        InsertCategoryTableAsyncTask(CategoryTableDao categoryTableDao) {
            this.categoryTableDao = categoryTableDao;
        }

        @Override
        protected Void doInBackground(CategoryTableEntity... categoryTableEntities) {
            categoryTableDao.insertCategoryTable(categoryTableEntities[0]);
            return null;
        }
    }

    public LiveData<List<CategoryTableEntity>> getAllCategoriesTable() {
        return categoryTableDao.getAllCategoryTable();
    }

}
