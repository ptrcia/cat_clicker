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


//region Metodo de la muerte
    private final MediatorLiveData<Map<ClickUpgrade, Level>> filteredUpgrades = new MediatorLiveData<>();

    public LiveData<Map<ClickUpgrade, Level>> getUpgradesTypeUserLevel (String type, String userId){
        try {
            LiveData<List<ClickUpgrade>> upgradesByType = getUpgradesType(type); //mejoras por tipo

        //al mapa le metemos la lista de mejoas por tpo
            filteredUpgrades.addSource(upgradesByType, upgradesMuted -> {  //cuando mute upgradeList se actualizará
                Map<ClickUpgrade, Level> upgradesMap = new HashMap<>();

                for (ClickUpgrade upgrade : upgradesMuted) { //recorremos cada mejora
                    Log.d("Clicker-> ", "FILTRADAS POR TIPO " + type + " -> ID: " + upgrade.getId() + //LO VEMOS
                            ", Name: " + upgrade.getName() +
                            ", Description: " + upgrade.getDescription() +
                            ", Type: " + upgrade.getType() +
                            ", Level: " + upgrade.getLevel());
                    String idUpgrades = upgrade.getId(); //cogemos el id

                    LiveData<String> currentLevelOfUpgrade = userRepository.getUserLevel(idUpgrades);  //por cada mejora cogemos su nivel

                    filteredUpgrades.addSource(currentLevelOfUpgrade, levelCurrentMuted -> {  //cuando mute currentLevelOfUpgrade se actualizará
                        Log.d("Clicker-> ", "LA MEJORA "  + upgrade.getId() + "  DE TIPO  " +type+" . LA TIENE EL USER A NIVEL -> "  + levelCurrentMuted);
                        if (levelCurrentMuted == null) {
                            Log.d("Clicker-> ", "currentLevel es nulll " + idUpgrades);
                            return;
                        }

                        int nextLevel = Integer.parseInt(levelCurrentMuted )+ 1; //obtenermos el siguiente nivel
                        Log.d("Clicker->", "El  nextLevel: para la mejora " + idUpgrades + " es " + nextLevel);

                        //buscamos las mejoras del siguiente nivel

                        LiveData<Level> levelNexUser = upgradesRepository.getLevelForUpgradeByUserLevel(idUpgrades, String.valueOf(nextLevel)); //dame los niveles del siguiente nivel del jugador
                        //entender awui.
                        //que pasa en los mapas

                        filteredUpgrades.addSource(levelNexUser, level -> { //cuando se actualiza será level
                            if (level == null) {
                                Log.d("Clicker-> ", "levelNexUser es NULL o NO HAY MAS NIVELES. Pasamos al siguiente.");
                                return;
                            } else {
                                Log.d("Clicker-> ", "!!!!!!!!!!!!!!NIVEL RECIBIDO DE LIVEDATA  -> ID" + level.getIdLevel() + " COSTE  " + level.getCost() + " EFFECTO " + level.getEffect());
                            }
                            upgradesMap.put(upgrade, level);
                            Log.d("Clicker->", "!!!!!!!!!!upgradesmap actualizado: " + upgradesMap.toString());//esto es null
                            //filteredUpgrades.setValue(upgradesMap);
                                if (upgradesMap.size() == upgradesMuted.size()) {
                                    filteredUpgrades.setValue(upgradesMap);
                                    //ver que tiene el filteresupgrades
                                    Log.d("Clicker->", "!!!!!!!!!!!!!!filteripgrades actualizado: " + upgradesMap.toString());
                                }

                        });
                    });
                }
            });
        } catch (Exception e) {
            Log.d("Clicker->", "Error al obtener las mejoras por tipo: " + e.getMessage());
            return filteredUpgrades; // En caso de error, retornar el MediatorLiveData vacío
        }
           return filteredUpgrades;
     }
    //endregion



 /*   private final MediatorLiveData<Map<ClickUpgrade, Level>> filteredUpgrades = new MediatorLiveData<>();

    public LiveData<Map<ClickUpgrade, Level>> getUpgradesTypeUserLevel(String type, String userId) {
        try {
            // Obtener las mejoras por tipo (solo una vez)
            LiveData<List<ClickUpgrade>> upgradesByType = getUpgradesType(type);

            // Agregar la fuente solo si no está ya añadida
            filteredUpgrades.addSource(upgradesByType, upgradesMuted -> {
                Map<ClickUpgrade, Level> upgradesMap = new HashMap<>();

                // Recorrer las mejoras por tipo
                for (ClickUpgrade upgrade : upgradesMuted) {
                    Log.d("Clicker->", "FILTRADAS POR TIPO " + type + " -> ID: " + upgrade.getId() +
                            ", Name: " + upgrade.getName() +
                            ", Description: " + upgrade.getDescription() +
                            ", Type: " + upgrade.getType() +
                            ", Level: " + upgrade.getLevel());

                    String idUpgrades = upgrade.getId(); // Obtener el ID de la mejora

                    // Obtener el nivel actual del usuario para la mejora
                    LiveData<String> currentLevelOfUpgrade = userRepository.getUserLevel(idUpgrades);

                    // Agregar la fuente solo si no está ya añadida
                    filteredUpgrades.addSource(currentLevelOfUpgrade, levelCurrentMuted -> {
                        Log.d("Clicker->", "La mejora " + upgrade.getId() + " tiene el nivel " + levelCurrentMuted);

                        if (levelCurrentMuted == null) {
                            Log.d("Clicker->", "currentLevel es null para " + idUpgrades);
                            return; // Si el nivel actual es null, saltamos al siguiente upgrade
                        }

                        // Calcular el siguiente nivel
                        int nextLevel = Integer.parseInt(levelCurrentMuted) + 1;
                        Log.d("Clicker->", "El siguiente nivel para la mejora " + idUpgrades + " es " + nextLevel);

                        // Obtener el nivel siguiente del usuario
                        LiveData<Level> levelNextUser = upgradesRepository.getLevelForUpgradeByUserLevel(idUpgrades, String.valueOf(nextLevel));

                        // Agregar la fuente solo si no está ya añadida
                        filteredUpgrades.addSource(levelNextUser, level -> {
                            if (level == null) {
                                Log.d("Clicker->", "No hay más niveles para la mejora " + idUpgrades);
                                return; // Si no hay más niveles, seguimos con el siguiente upgrade
                            } else {
                                Log.d("Clicker->", "Nivel recibido para la mejora " + idUpgrades + ": " + level.getIdLevel() + " con coste " + level.getCost());
                            }

                            // Actualizar el mapa de mejoras con su respectivo nivel
                            upgradesMap.put(upgrade, level);

                            // Solo actualizar filteredUpgrades cuando todos los upgrades hayan sido procesados
                            if (upgradesMap.size() == upgradesMuted.size()) {
                                filteredUpgrades.setValue(upgradesMap);
                                Log.d("Clicker->", "filteredUpgrades actualizado: " + upgradesMap.toString());
                            }
                        });
                    });
                }
            });
        } catch (Exception e) {
            Log.d("Clicker->", "Error al obtener las mejoras por tipo: " + e.getMessage());
            return filteredUpgrades; // En caso de error, retornar el MediatorLiveData vacío
        }

        return filteredUpgrades;
    }*/

}