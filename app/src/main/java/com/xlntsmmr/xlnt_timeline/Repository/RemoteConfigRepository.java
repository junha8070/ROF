package com.xlntsmmr.xlnt_timeline.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RemoteConfigRepository {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    MutableLiveData<Boolean> isRemoteConfigLoadFinish;

    public RemoteConfigRepository() {
        isRemoteConfigLoadFinish = new MutableLiveData<>();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // 캐시된 값을 사용할 시간 설정 (초)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    public void fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        isRemoteConfigLoadFinish.setValue(true);
                        // Remote Config 값을 가져와서 사용할 수 있음
                    }else {
                        isRemoteConfigLoadFinish.setValue(false);
                    }
                });
    }

    public boolean getForceUpdate() {
        return mFirebaseRemoteConfig.getBoolean("force_update");
    }

    public String getLatestVersion() {
        return mFirebaseRemoteConfig.getString("latest_ver");
    }

    public String getMinVersion() {
        return mFirebaseRemoteConfig.getString("min_ver");
    }

    public String getNewFunction(){
        return mFirebaseRemoteConfig.getString("new_function");
    }

    public String getUpdateNews(){
        return mFirebaseRemoteConfig.getString("update_news");
    }

    public LiveData<Boolean> getIsRemoteConfigLoadFinish() {
        return isRemoteConfigLoadFinish;
    }
}