package com.xlntsmmr.xlnt_timeline.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.xlntsmmr.xlnt_timeline.Dao.CategoryDao;
import com.xlntsmmr.xlnt_timeline.Entity.CategoryEntity;
import com.xlntsmmr.xlnt_timeline.Dao.TimeLineDao;
import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;

@Database(entities = {CategoryEntity.class, TimeLineEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();

    public abstract TimeLineDao timeLineDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "com.xlntsmmr.rof")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}