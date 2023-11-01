package com.xlntsmmr.xlnt_timeline.DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.xlntsmmr.xlnt_timeline.Dao.CategoryDao;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Dao.CategoryTableDao;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryTableEntity;
import com.xlntsmmr.xlnt_timeline.Dao.SettingDao;
import com.xlntsmmr.xlnt_timeline.Entity.SettingEntity;
import com.xlntsmmr.xlnt_timeline.Dao.TimeLineDao;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;

@Database(entities = {CategoryEntity.class, TimeLineEntity.class, CategoryTableEntity.class, SettingEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();

    public abstract TimeLineDao timeLineDao();

    public abstract SettingDao settingDao();

    public abstract CategoryTableDao categoryTableDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addCallback(roomCallback) // 이 부분을 추가합니다.
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // AppDatabase가 처음 생성될 때 호출됩니다.
            // 여기서 초기 데이터를 추가할 수 있습니다.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CategoryDao categoryDao;
        private final SettingDao settingDao;

        PopulateDbAsync(AppDatabase db) {
            categoryDao = db.categoryDao();
            settingDao = db.settingDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // 여기서 초기 데이터를 추가합니다.
//            CategoryEntity category = new CategoryEntity(UUID.randomUUID().toString(), "#FFFFFF", "전체");
//            categoryDao.insertCategory(category);

            SettingEntity setting = new SettingEntity();
            setting.setDate_skip(false);
            setting.setAlarm(false);
            settingDao.insertSetting(setting);
            // 추가할 다른 초기 카테고리들도 동일한 방식으로 추가합니다.
            return null;
        }
    }
}