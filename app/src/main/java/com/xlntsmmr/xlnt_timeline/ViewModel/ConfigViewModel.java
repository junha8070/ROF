package com.xlntsmmr.xlnt_timeline.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.xlntsmmr.xlnt_timeline.Repository.RemoteConfigRepository;

public class ConfigViewModel extends ViewModel {
    private RemoteConfigRepository mRemoteConfigRepository;

    private LiveData<Boolean> isRemoteConfigLoadFinish;

    public ConfigViewModel() {
        mRemoteConfigRepository = new RemoteConfigRepository();
    }

    public void fetchRemoteConfig() {
        mRemoteConfigRepository.fetchRemoteConfig();
    }

    public boolean getForceUpdate() {
        return mRemoteConfigRepository.getForceUpdate();
    }

    public String getLatestVersion() {
        return mRemoteConfigRepository.getLatestVersion();
    }

    public String getMinVersion() {
        return mRemoteConfigRepository.getMinVersion();
    }

    public String getUpdateNews() {
        return mRemoteConfigRepository.getUpdateNews();
    }

    public String getNewFunction() {
        return mRemoteConfigRepository.getNewFunction();
    }

    public LiveData<Boolean> getIsRemoteConfigLoadFinish() {
        return mRemoteConfigRepository.getIsRemoteConfigLoadFinish();
    }
}
