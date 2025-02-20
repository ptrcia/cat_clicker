package com.example.android_app.RoomDB;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.app.Application;
import android.util.Log;

import java.util.List;

public class UpgradeFragmentViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;
    private final LiveData<List<ClickUpgrade>> allUpgrades;


    // Constructor
    public UpgradeFragmentViewModel(Application application) {
        super(application);
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);
        allUpgrades = upgradesRepository.getAllUpgrades();

    }

    // insetar click en el repositorio
    public void insertClickUpgrade(ClickUpgrade clickUpgrade) {
        upgradesRepository.insert(clickUpgrade);
    }

    public LiveData<List<ClickUpgrade>> getUpgradesType(String type) {
        Log.d("Clicker-> ViewModel", "Fetching upgrades for type: " + type);

        return upgradesRepository.getClickUpgradeByType(type);
    }

    public LiveData<List<Level>> getLevelsForUpgrade(String idUpgrade) {
        return upgradesRepository.getLevelsForUpgrade(idUpgrade);
    }

    public LiveData<List<UpgradesUser>> getAllUserUpgrades(String userId) {
        return userRepository.getAllUserUpgrades(userId);
    }

    public LiveData<Integer> getUserUpgradeLevel(String userId, String upgradeId) {
        return userRepository.getUserUpgradeLevel(userId, upgradeId);
    }

    //get avaliable upgrades
    public LiveData<List<ClickUpgrade>> getAvailableUpgrades(String idUser) {
        return upgradesRepository.getAvailableUpgrades(idUser);
    }
}
