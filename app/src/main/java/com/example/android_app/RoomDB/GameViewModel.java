package com.example.android_app.RoomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GameViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;
    public final MutableLiveData<UserStats> userStatsLiveData = new MutableLiveData<>();

    public GameViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);
    }

    public void insertClickUpgrade(ClickUpgrade upgrade) {
         upgradesRepository.insert(upgrade);
    }

    public ClickUpgrade getClickUpgradeByKey(String id) {
        return upgradesRepository.getClickUpgradeById(id);
    }

    public void getUserStats(String userId) {
        userRepository.getUserStats(userId, new BaseCallback(){

            @Override
            public void onSuccess(UserStats userStats) {
                userStatsLiveData.postValue(userStats);
            }

            @Override
            public void onError() {

            }
        });
    }
}
