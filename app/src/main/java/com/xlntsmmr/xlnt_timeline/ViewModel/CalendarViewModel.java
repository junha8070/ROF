package com.xlntsmmr.xlnt_timeline.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {
    private MutableLiveData<String> selectedUuid = new MutableLiveData<>();
//    private MutableLiveData<Integer> selectedOldPosition = new MutableLiveData<>();

    public LiveData<String> getSelectedPosition() {
        return selectedUuid;
    }

    public void setSelectedPosition(String uuid) {
        selectedUuid.setValue(uuid);
    }

//    public LiveData<Integer> getSelectedOldPosition() {
//        return selectedOldPosition;
//    }
//
//    public void setSelectedOldPosition(int oldPosition) {
//        selectedOldPosition.setValue(oldPosition);
//    }
}

