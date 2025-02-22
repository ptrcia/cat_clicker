package com.example.android_app.RoomDB;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import android.app.Application;
import android.util.Log;

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

    public LiveData<String> getUserUpgradeLevel(String userId, String upgradeId) {
        return userRepository.getUserUpgradeLevel(userId, upgradeId);
    }

    //get avaliabble upgrades
    public LiveData<List<ClickUpgrade>> getAvailableUpgrades(String idUser) {
        return upgradesRepository.getAvailableUpgrades(idUser);
    }
/*
    // get all available by user level type
    public LiveData<Map<ClickUpgrade,Level>> getUpgradesTypeUserLevel(String type, String userId) {

        //Murable data
        MutableLiveData<Map<ClickUpgrade, Level>> filteredUpgrades = new MutableLiveData<>(new HashMap<>());
        List<ClickUpgrade> upgradesType = this.getUpgradesType(type).getValue(); //esta lista está vacia por alguna razon


  /*     // Sacar el nivel del usuario
        String currentLevel = userRepository.getUserLevel(idUpgrade).getValue();
        String nextLevel;
        try {
            nextLevel = String.valueOf(Integer.parseInt(currentLevel) + 1);
        }catch (Exception e){
            Log.d("Clicker-> ", "Error al obtener el nivel del usuario de la mejora " + "idUpgrade");
            Log.d("Clicker-> ", "currentLevel: " + currentLevel);
            return filteredUpgrades;
        }

        for(ClickUpgrade upgrade : upgradesType){//upgradetypenull
            List<String> upgradeId= new ArrayList<>();
            upgradeId.add(upgrade.getId());
            String idUpgrades = upgrade.getId();

            String currentLevel = userRepository.getUserLevel(idUpgrades).getValue();
            String nextLevel;
            try {
                nextLevel = String.valueOf(Integer.parseInt(currentLevel) + 1);
            }catch (Exception e) {
                Log.d("Clicker-> ", "Error al obtener el nivel del usuario de la mejora " + "idUpgrade");
                Log.d("Clicker-> ", "currentLevel: " + currentLevel);
                return filteredUpgrades;
            }
            Level  level = upgradesRepository.getLevelForUpgradeByLevel(upgrade.getId(), nextLevel).getValue();
            filteredUpgrades.getValue().put(upgrade, level);
        }
            return filteredUpgrades;
    }
}*/

    private final MediatorLiveData<Map<ClickUpgrade, Level>> filteredUpgrades = new MediatorLiveData<>();

    public LiveData<Map<ClickUpgrade, Level>> getUpgradesTypeUserLevel (String type, String userId){
        //try catch para que no se crashee
        try {LiveData<List<ClickUpgrade>> upgradesByType = getUpgradesType(type);}
        catch (Exception e){
            Log.d("Clicker-> ", "Error al obtener las mejoras por tipo");
            return filteredUpgrades;
        }
        LiveData<List<ClickUpgrade>> upgradesByType = getUpgradesType(type); //mejoras por tipo
        //quiero observar los resultados de upgradeLiveData
        upgradesByType.observeForever(upgradesList -> { //esto lo tengo que eliminar en el futuro porque consume memoria
            for (ClickUpgrade upgrade : upgradesList) {
                Log.d("Clicker-> ", "FILTRADAS POR TIPO " + type +" -> ID: " + upgrade.getId() +
                        ", Name: " + upgrade.getName() +
                        ", Description: " + upgrade.getDescription() +
                        ", Type: " + upgrade.getType() +
                        ", Level: " + upgrade.getLevel());
            }
        });
    //al mapa le metemos la lista de mejoas por tpo
        filteredUpgrades.addSource(upgradesByType, upgradesMuted -> {  //cuando mute upgradeList se actualizará
            Map<ClickUpgrade, Level> upgradesMap = new HashMap<>();

            for (ClickUpgrade upgrade : upgradesMuted) { //recorremos cada mejora
                String idUpgrades = upgrade.getId(); //cogemos el id

                LiveData<String> currentLevelOfUpgrade = userRepository.getUserLevel(idUpgrades);  //por cada mejora cogemos su nivel

                //lo observi para ver que nivel tiene el usuario de cada mejora
                currentLevelOfUpgrade.observeForever(levelMuted -> {
                    Log.d("Clicker-> ", "LA MEJORA "  + upgrade.getId() + "  DE TIPO  " +type+" . LA TIENE EL USER A NIVEL -> "  + levelMuted);
                });

                filteredUpgrades.addSource(currentLevelOfUpgrade, levelCurrentMuted -> {  //cuando mute currentLevelOfUpgrade se actualizará
                    if (levelCurrentMuted == null) {
                        Log.d("Clicker-> ", "currentLevel es nulll " + idUpgrades);
                        return;
                    }

                    int nextLevel = Integer.parseInt(levelCurrentMuted )+ 1; //obtenermos el siguiente nivel
                    Log.d("Clicker->", "El  nextLevel: para la mejora " + idUpgrades + " es " + nextLevel);

                    //buscamos las mejoras del siguiente nivel

                    LiveData<Level> levelNexUser = upgradesRepository.getLevelForUpgradeByUserLevel(idUpgrades, String.valueOf(nextLevel)); //dame los niveles del siguiente nivel del jugador
                    levelNexUser.observeForever(level -> {
                        if (level == null) {
                            Log.d("Clicker-> ", "levelNexUser es null o no hay más niveles. Pasamos al siguiente.");
                            return;
                        } else {
                            Log.d("Clicker-> ", "!!!!!!!!!!!!!!NIVEL RECIBIDO DE LIVEDATA  ->" + level);
                        }
                    });

                    //entender awui.
                    //que pasa en los mapas

                    filteredUpgrades.addSource(levelNexUser, level -> { //cuando se actualiza será level
                        upgradesMap.put(upgrade, level);
                        Log.d("Clicker->", "!!!!!!!!!!upgradesmap actualizado: " + upgradesMap);//esto es null
                        filteredUpgrades.setValue(upgradesMap);

                        //ver que tiene el filteresupgrades
                        filteredUpgrades.observeForever(upgrades -> {
                            Log.d("Clicker->" ,  "!!!!!!!!!!!!!!filteripgrades actualizado: " + upgrades);
                        });
                    });
                });
            }
        });

        return filteredUpgrades;
    }
    }