package com.xlntsmmr.xlnt_timeline.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xlntsmmr.xlnt_timeline.Entity.TimeLineEntity;
import com.xlntsmmr.xlnt_timeline.Repository.TimeLineRepository;

import java.util.List;

public class TimeLineViewModel extends AndroidViewModel {

    private TimeLineRepository timeLineRepository;
    private LiveData<List<TimeLineEntity>> allTimelines;


    public TimeLineViewModel(@NonNull Application application) {
        super(application);
        timeLineRepository = new TimeLineRepository(application);
        allTimelines = timeLineRepository.getAllTimeLine();
    }

    public LiveData<List<TimeLineEntity>> getAllTimelines() {
        return allTimelines;
    }
    public void insertTimeLine(TimeLineEntity timeLine){
        timeLineRepository.insertTimeLine(timeLine);
    }

    public void deleteTimeLineByUUID(String uuid) {
        timeLineRepository.deleteTimeLineByUUID(uuid);
    }

    public void updateStatusByUUID(String uuid, int newStatus) {
        timeLineRepository.updateStatusByUUID(uuid, newStatus);
    }

    public LiveData<TimeLineEntity> getTimeLineByUUID(String uuid) {
        return timeLineRepository.getTimeLineByUUID(uuid);
    }

    public void updateTimeLine(TimeLineEntity timeLine) {
        timeLineRepository.updateTimeLine(timeLine);
    }

    public void updateCategoryByCategoryUUID(String categoryUUID, String newCategory) {
        timeLineRepository.updateCategoryByCategoryUUID(categoryUUID, newCategory);
    }

    public void deleteTimelinesByCategoryUUID(String categoryUUID) {
        timeLineRepository.deleteTimelinesByCategoryUUID(categoryUUID);
    }


}
