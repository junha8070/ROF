package com.xlntsmmr.xlnt_timeline.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.xlntsmmr.xlnt_timeline.Repository.RemoteConfigRepository;

public class ConfigViewModel extends ViewModel {
    private RemoteConfigRepository mRemoteConfigRepository;

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

    public String getUpdateNewsJson() {
        return mRemoteConfigRepository.getUpdateNewsJson();
    }

    public String getJsonLatestVersion(){
        return mRemoteConfigRepository.getJsonLatestVersion();
    }

    public String getJsonUpdateNews(){
        return mRemoteConfigRepository.getJsonUpdateNews();
    }

    public String getJsonNewFunction(){
        return mRemoteConfigRepository.getJsonNewFunction();
    }
}
