package com.example.android_app.RoomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class GameViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;

    public GameViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);
    }

    public void insertClickUpgrade(ClickUpgrade upgrade) {
         upgradesRepository.insert(upgrade);
    }

    public LiveData<ClickUpgrade> getClickUpgradeByKey(String id) {
        return upgradesRepository.getClickUpgradeById(id);
    }

    public LiveData<UserStats> getUserStats(String userId) {
        return userRepository.getUserStats(userId);
    }
}
