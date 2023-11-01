package com.xlntsmmr.xlnt_timeline.Repository;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xlntsmmr.xlnt_timeline.DTO.ConfigUpdateNewsDTO;

import java.util.List;

public class RemoteConfigRepository {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    Gson gson;

    public RemoteConfigRepository() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // 캐시된 값을 사용할 시간 설정 (초)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        gson = new Gson();
    }

    public void fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Remote Config 값을 가져와서 사용할 수 있음
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

    public String getUpdateNewsJson(){
        return mFirebaseRemoteConfig.getString("update_news");
    }

    public List<ConfigUpdateNewsDTO> configUpdateNewsDTO(String updateNewsJsonString){
        return gson.fromJson(updateNewsJsonString, new TypeToken<List<ConfigUpdateNewsDTO>>(){}.getType());
    }

    public String getJsonLatestVersion(){
        return configUpdateNewsDTO(getUpdateNewsJson()).get(configUpdateNewsDTO(getUpdateNewsJson()).size()-1).getLatest_ver();
    }

    public String getJsonUpdateNews(){
        return configUpdateNewsDTO(getUpdateNewsJson()).get(configUpdateNewsDTO(getUpdateNewsJson()).size()-1).getUpdate_news();
    }

    public String getJsonNewFunction(){
        return configUpdateNewsDTO(getUpdateNewsJson()).get(configUpdateNewsDTO(getUpdateNewsJson()).size()-1).getNew_function();
    }

}