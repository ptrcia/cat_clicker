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

    public void updateUserLevel(String idUpgrade, String level, String type, String userId){
        Log.d("Clicker->", "SE AH MEJORADO EL idUpgrade: " + idUpgrade + " al level: " + level);
        userRepository.updateUserLevel(idUpgrade, level);
    }

//region Metodo actualizar mejoras
    public void getUpgradesTypeUserLevel (String type, String userId){
        try {

            Map<ClickUpgrade, Level>auxMap = new HashMap<>();
            upgradesRepository.getClickUpgradeByType(type, new BaseCallback<List<ClickUpgrade>>() {
                @Override
                public void onSuccess(List<ClickUpgrade> upgrades) {
                    for (ClickUpgrade upgrade : upgrades) {
                        String idUpgrades = upgrade.getId();
                        //Log.d("Clicker->", "Mejoras filtradas por tipo " + type + ": " + idUpgrades); //"idUpgrades: ua_1"

                        userRepository.getUserLevel(idUpgrades, new BaseCallback<String>() {
                            @Override
                            public void onSuccess(String currentLevel) {
                                //Log.d("Clicker->", "Nivel al que la tiene el isuario la mejora  " + idUpgrades + ": " + currentLevel); //"currentLevel: 1"

                                //hay un errror en el parse int porque currentlevel es null
                                String currentLevelOfUpgrade = currentLevel; //es redundante
                                int nextLevel = Integer.parseInt(currentLevelOfUpgrade) + 1; //obtenermos el siguiente nivel
                                Log.d("Clicker->", "Siguiente nivel de la mejora " + idUpgrades + ": " + nextLevel);

                                upgradesRepository.getLevelForUpgradeByUserLevel(idUpgrades, String.valueOf(nextLevel), new BaseCallback<Level>() {
                                    @Override
                                    public void onSuccess(Level levelNextUser) {
                                        //Log.d("Clicker->", "levelNextUser: " + levelNextUser); //"levelNextUser: levelActive1_2"
                                        if(levelNextUser!=null){
                                           // auxMap.put(upgrade, levelNextUser);
                                            synchronized (auxMap) {  //ESTO ES IMPORTANTE PARA EVITAR ERRORES DE CONCURRENCIA
                                                auxMap.put(upgrade, levelNextUser);
                                                //Log.d("Clicker->", "auxMap: " + auxMap);
                                            }
                                        }

                                        if(upgrades.size()-1 == upgrades.indexOf(upgrade)){
                                            filteredUpgrades.postValue(auxMap);
                                            //filteredUpgrades.postValue(sortUpgradesById(auxMap));

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