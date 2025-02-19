package com.example.android_app.RoomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

public class GameViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;
    ExecutorService executorService;

    public GameViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);
    }

    public void insertClickUpgrade(ClickUpgrade upgrade) {
        executorService.execute(() -> upgradesRepository.insert(upgrade));
    }

    public LiveData<ClickUpgrade> getClickUpgradeByKey(String id) {
        return upgradesRepository.getClickUpgradeById(id);
    }

    public LiveData<UserStats> getUserStats(int userId) {
        return userRepository.getUserStats(userId);
    }
}
