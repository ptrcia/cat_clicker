package com.example.android_app.RoomDB;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeFragmentViewModel extends AndroidViewModel {
    private final UpgradesRepository upgradesRepository;
    private final UserRepository userRepository;
    public MutableLiveData<String> pollo = new MutableLiveData<>("Hola");
    public final MutableLiveData<Map<ClickUpgrade, Level>> filteredUpgrades = new MutableLiveData<>();


    // Constructor
    public UpgradeFragmentViewModel(Application application) {
        super(application);
        //cosas que coges de base
        upgradesRepository = new UpgradesRepository(application);
        userRepository = new UserRepository(application);
    }

//region Metodo

    public void getUpgradesTypeUserLevel (String type, String userId){
        try {

            Map<ClickUpgrade, Level>auxMap = new HashMap<>();
            upgradesRepository.getClickUpgradeByType(type, new BaseCallback<List<ClickUpgrade>>() {
                @Override
                public void onSuccess(List<ClickUpgrade> upgrades) {
                    for (ClickUpgrade upgrade : upgrades) {
                        String idUpgrades = upgrade.getId();

                        userRepository.getUserLevel(idUpgrades, new BaseCallback<String>() {
                            @Override
                            public void onSuccess(String currentLevel) {
                                String currentLevelOfUpgrade = currentLevel; //es redundante
                                int nextLevel = Integer.parseInt(currentLevelOfUpgrade) + 1; //obtenermos el siguiente nivel

                                upgradesRepository.getLevelForUpgradeByUserLevel(idUpgrades, String.valueOf(nextLevel), new BaseCallback<Level>() {
                                    @Override
                                    public void onSuccess(Level levelNextUser) {
                                        if(levelNextUser!=null){
                                            auxMap.put(upgrade, levelNextUser);
                                        }
                                        if(upgrades.size()-1 == upgrades.indexOf(upgrade)){
                                            filteredUpgrades.postValue(auxMap);
                                        }
                                    }
                                });
                            }
                        });
                    }
                    pollo.postValue("Juan");
                }
            });
        } catch (Exception e) {
            Log.d("Clicker->", "Error al obtener las mejoras por tipo: " + e.getMessage());
        }
    }
}