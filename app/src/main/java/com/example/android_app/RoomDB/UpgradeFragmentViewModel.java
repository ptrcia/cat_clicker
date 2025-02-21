package com.example.android_app.RoomDB;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeFragmentViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;


    // Constructor
    public UpgradeFragmentViewModel(Application application) {
        super(application);
        //cosas que coges de base
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);

    }
//se acualizan
    //return con el upgraderepositury
    // insetar click en el repositorio
    public void insertClickUpgrade(ClickUpgrade clickUpgrade) {
        upgradesRepository.insert(clickUpgrade);
    }

    public LiveData<List<ClickUpgrade>> getUpgradesType(String type) {
        return upgradesRepository.getClickUpgradeByType(type);
    }
    public LiveData<List<ClickUpgrade>> getAllUpgrades() {
        return upgradesRepository.getAllUpgrades();
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

    // get all available by user level and type
    public LiveData<Map<ClickUpgrade,Level>> getUpgradesTypeUserLevel(String type, String userId) {
        // meter un mismo array o lista que sea parejas de upgrades y levels

        //List<ClickUpgrade,Level> upgrades = new ArrayList<>();
        MutableLiveData<Map<ClickUpgrade, Level>> filteredUpgrades = new MutableLiveData<>(new HashMap<>());
        List<ClickUpgrade> upgradesType = this.getUpgradesType(type).getValue();

        // Sacar el nivel del usuario
        String currentLevel = userRepository.getUserLevel(userId).getValue();
        String nextLevel = String.valueOf(Integer.parseInt(currentLevel) + 1);

        for(ClickUpgrade upgrade : upgradesType){
            Level  level = upgradesRepository.getLevelForUpgradeByLevel(upgrade.getId(), nextLevel).getValue();
            filteredUpgrades.getValue().put(upgrade, level);
        }

        return filteredUpgrades;
    }

    //
}
