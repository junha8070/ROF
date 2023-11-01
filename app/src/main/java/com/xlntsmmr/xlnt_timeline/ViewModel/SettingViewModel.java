package com.xlntsmmr.xlnt_timeline.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xlntsmmr.xlnt_timeline.Entity.SettingEntity;
import com.xlntsmmr.xlnt_timeline.Repository.SettingRepository;

public class SettingViewModel extends AndroidViewModel {

    private SettingRepository repository;

    private LiveData<SettingEntity> allSettings;


    public SettingViewModel(@NonNull Application application) {
        super(application);
        repository = new SettingRepository(application);
        allSettings = repository.getAllSettings();
    }

    public LiveData<SettingEntity> getAllSettings() {
        return allSettings;
    }

    public void insertSetting(SettingEntity setting){
        repository.insertSetting(setting);
    }

    public void updateSetting(SettingEntity setting) {
        repository.updateSetting(setting);
    }

}
