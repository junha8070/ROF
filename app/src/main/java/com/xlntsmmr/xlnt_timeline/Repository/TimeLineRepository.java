package com.xlntsmmr.xlnt_timeline.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.xlntsmmr.xlnt_timeline.Dao.TimeLineDao;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.DB.AppDatabase;

import java.util.List;

public class TimeLineRepository {

    String TAG = "TimeLineRepository";

    private TimeLineDao timeLineDao;

    public TimeLineRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        timeLineDao = db.timeLineDao();
    }

    public void insertTimeLine(TimeLineEntity timeLine) {
        new InsertTimeLineAsyncTask(timeLineDao).execute(timeLine);
    }

    private static class InsertTimeLineAsyncTask extends AsyncTask<TimeLineEntity, Void, Void> {

        private TimeLineDao timeLineDao;

        InsertTimeLineAsyncTask(TimeLineDao timeLineDao) {
            this.timeLineDao = timeLineDao;
        }

        @Override
        protected Void doInBackground(TimeLineEntity... timeLineEntities) {
            timeLineDao.insertTimeLine(timeLineEntities[0]);
            return null;
        }
    }

    public void updateCategoryByCategoryUUID(String categoryUUID, String newCategory) {
        new UpdateCategoryByCategoryUUIDAsyncTask(timeLineDao).execute(categoryUUID, newCategory);
    }

    private static class UpdateCategoryByCategoryUUIDAsyncTask extends AsyncTask<String, Void, Void> {
        private TimeLineDao timeLineDao;

        UpdateCategoryByCategoryUUIDAsyncTask(TimeLineDao timeLineDao) {
            this.timeLineDao = timeLineDao;
        }

        @Override
        protected Void doInBackground(String... params) {
            String categoryUUID = params[0];
            String newCategory = params[1];
            timeLineDao.updateCategoryByCategoryUUID(categoryUUID, newCategory);
            return null;
        }
    }

    public void deleteTimelinesByCategoryUUID(String categoryUUID) {
        new DeleteTimelinesByCategoryUUIDAsyncTask(timeLineDao).execute(categoryUUID);
    }

    private static class DeleteTimelinesByCategoryUUIDAsyncTask extends AsyncTask<String, Void, Void> {
        private TimeLineDao timeLineDao;

        DeleteTimelinesByCategoryUUIDAsyncTask(TimeLineDao timeLineDao) {
            this.timeLineDao = timeLineDao;
        }

        @Override
        protected Void doInBackground(String... params) {
            String categoryUUID = params[0];
            timeLineDao.deleteTimelinesByCategoryUUID(categoryUUID);
            return null;
        }
    }


    public LiveData<List<TimeLineEntity>> getAllTimeLine() {
        return timeLineDao.getAllTimeLine();
    }

    public void deleteTimeLineByUUID(String uuid) {
        new DeleteTimeLineByUUIDAsyncTask(timeLineDao).execute(uuid);
    }

    private static class DeleteTimeLineByUUIDAsyncTask extends AsyncTask<String, Void, Void> {

        private TimeLineDao timeLineDao;

        DeleteTimeLineByUUIDAsyncTask(TimeLineDao timeLineDao) {
            this.timeLineDao = timeLineDao;
        }

        @Override
        protected Void doInBackground(String... uuids) {
            timeLineDao.deleteTimeLineByUUID(uuids[0]);
            return null;
        }
    }

    public void updateStatusByUUID(String uuid, int newStatus) {
        new UpdateStatusByUUIDAsyncTask(timeLineDao).execute(uuid, String.valueOf(newStatus));
    }

    private static class UpdateStatusByUUIDAsyncTask extends AsyncTask<String, Void, Void> {
        private TimeLineDao timeLineDao;

        UpdateStatusByUUIDAsyncTask(TimeLineDao timeLineDao) {
            this.timeLineDao = timeLineDao;
        }

        @Override
        protected Void doInBackground(String... params) {
            String uuid = params[0];
            int newStatus = Integer.parseInt(params[1]);
            timeLineDao.updateStatusByUUID(uuid, newStatus);
            return null;
        }
    }

    public LiveData<TimeLineEntity> getTimeLineByUUID(String uuid) {
        return timeLineDao.getTimeLineByUUID(uuid);
    }

    public void updateTimeLine(TimeLineEntity timeLine) {
        Log.d(TAG, timeLine.getContents());
        new UpdateTimeLineAsyncTask(timeLineDao).execute(timeLine);
    }

    private static class UpdateTimeLineAsyncTask extends AsyncTask<TimeLineEntity, Void, Void> {
        private TimeLineDao timeLineDao;

        UpdateTimeLineAsyncTask(TimeLineDao timeLineDao) {
            this.timeLineDao = timeLineDao;
        }

        @Override
        protected Void doInBackground(TimeLineEntity... timeLineEntities) {
            timeLineDao.updateTimeLine(timeLineEntities[0]);
            return null;
        }
    }


}
