package com.xlntsmmr.xlnt_timeline.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.xlntsmmr.xlnt_timeline.Dao.SettingDao;
import com.xlntsmmr.xlnt_timeline.Entity.SettingEntity;
import com.xlntsmmr.xlnt_timeline.DB.AppDatabase;

public class SettingRepository {

    String TAG = "SettingRepository";

    private SettingDao settingDao;

    public SettingRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        settingDao = db.settingDao();
    }

    public void insertSetting(SettingEntity setting) {
        new InsertSettingAsyncTask(settingDao).execute(setting);
    }

    private static class InsertSettingAsyncTask extends AsyncTask<SettingEntity, Void, Void> {

        private SettingDao settingDao;

        InsertSettingAsyncTask(SettingDao settingDao) {
            this.settingDao = settingDao;
        }

        @Override
        protected Void doInBackground(SettingEntity... settingEntities) {
            settingDao.insertSetting(settingEntities[0]);
            return null;
        }
    }

    public LiveData<SettingEntity> getAllSettings() {
        return settingDao.getAllSettings();
    }

    public void updateSetting(SettingEntity setting) {
        new UpdateSettingAsyncTask(settingDao).execute(setting);
    }

    private static class UpdateSettingAsyncTask extends AsyncTask<SettingEntity, Void, Void> {

        String TAG = "SettingRepositoryAsync";
        private SettingDao settingDao;

        UpdateSettingAsyncTask(SettingDao settingDao) {
            this.settingDao = settingDao;
        }

        @Override
        protected Void doInBackground(SettingEntity... settingEntities) {
            Log.d(TAG, "doInBackground");
            Log.d(TAG, "alarm: "+settingEntities[0].isAlarm());
            Log.d(TAG, "isDate_skip: "+settingEntities[0].isDate_skip());
            settingDao.updateSetting(settingEntities[0].isDate_skip(), settingEntities[0].isAlarm(), 1);
            return null;
        }
    }


}
