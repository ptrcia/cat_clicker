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

    public List<ClickUpgrade> getUpgradesType(String type) {
        return upgradesRepository.getClickUpgradeByType(type);
    }

//region Metodo

    public void getUpgradesTypeUserLevel (String type, String userId){
        try {

            Map<ClickUpgrade, Level>auxMap = new HashMap<>();
            List<ClickUpgrade> upgradesByType = getUpgradesType(type); //mejoras por tipo

            for(ClickUpgrade upgrade : upgradesByType){
                String idUpgrades = upgrade.getId();
                String currentLevelOfUpgrade = userRepository.getUserLevel(idUpgrades);

                int nextLevel = Integer.parseInt(currentLevelOfUpgrade )+ 1; //obtenermos el siguiente nivel

                Level levelNextUser = upgradesRepository.getLevelForUpgradeByUserLevel(idUpgrades, String.valueOf(nextLevel)); //dame los niveles del siguiente nivel del jugador
                auxMap.put(upgrade, levelNextUser);
            }
            filteredUpgrades.postValue(auxMap);
            pollo.postValue("Juan");

        } catch (Exception e) {
            Log.d("Clicker->", "Error al obtener las mejoras por tipo: " + e.getMessage());
        }
     }
    //endregion

}