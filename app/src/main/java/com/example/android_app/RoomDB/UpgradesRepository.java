package com.example.android_app.RoomDB;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.List;

public class UpgradesRepository {
    private final ClickUpgradeDAO clickUpgradeDAO;
    private final LevelDAO levelDAO;
    private final Executor executorService;


    public UpgradesRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        clickUpgradeDAO = db.clickUpgradeDAO();
        levelDAO = db.levelDAO();
        executorService = AppDataBase.databaseWriteExecutor;

        LiveData<List<ClickUpgrade>> allUpgrades = clickUpgradeDAO.getAllUpgrades();
        if (allUpgrades.getValue() == null || allUpgrades.getValue().isEmpty()) {
            Log.d("Clicker-> ", "Table is empty");
            upgradesTable();
        } else {
            Log.d("Clicker-> ", "Table is not empty");
        }

    }
    //region Introducir Datos
    //Meter datos en el ClickUpgrade
    public void insert(ClickUpgrade upgrade) {
        executorService.execute(() -> clickUpgradeDAO.insert(upgrade));
    }
    //Meter datos en el Levels
    public void insert(Level level) {
        executorService.execute(() -> levelDAO.insert(level));
    }
    //endregion

    //Upgrade por key
    public LiveData<ClickUpgrade> getClickUpgradeById(String id) {
        return clickUpgradeDAO.getClickUpgradeById(id);
    }

    //UPgrades por tipo
    public LiveData<List<ClickUpgrade>> getClickUpgradeByType(String type) {
        LiveData<List<ClickUpgrade>> getClickUpgradeByType = clickUpgradeDAO.getClickUpgradeByType(type);

        getClickUpgradeByType.observeForever(upgrades -> {
            if (upgrades != null) {
                //Log.d("Clicker -> ViewModel", "Datos obtenidos: " + upgrades);
            } else {
                //Log.d("Clicker -> ViewModel", "No hay datos en la tabla.");
            }
        });
        //return clickUpgradeDAO.getClickUpgradeByType(type);
        return getClickUpgradeByType;

    }


    //get all upgrades
    //ESTO ES TAMBIEN PARA EL TEXT
    public LiveData<List<ClickUpgrade>> getAllUpgrades() {
        LiveData<List<ClickUpgrade>> getAllUpgrades = clickUpgradeDAO.getAllUpgrades();
        getAllUpgrades.observeForever(upgrades -> {
            if (upgrades != null) {
                Log.d("Clicker -> ViewModel", "Datos obtenidos UPGRADE RESPOITORY: " + upgrades);
            } else {
                Log.d("Clicker -> ViewModel", "No hay datos en la tabla.");
            }
        });
        return getAllUpgrades;
    }
    //get active upgrades
    public LiveData<List<ClickUpgrade>> getActiveUpgrades() {
        return clickUpgradeDAO.getActiveUpgrades();
    }
    //get passive upgrades
    public LiveData<List<ClickUpgrade>> getPassiveUpgrades() {
        return clickUpgradeDAO.getPassiveUpgrades();
    }
    //levels
    public LiveData<List<Level>> getLevelsForUpgrade(String idUpgrade) {
        return levelDAO.getLevelsForUpgrade(idUpgrade);
    }
    //get avaliable upgrades
    public LiveData<List<ClickUpgrade>> getAvailableUpgrades(String idUser) {
        return clickUpgradeDAO.getAvailableUpgrades(idUser);
    }

    public void upgradesTable() {

        executorService.execute(() -> {
            ClickUpgrade defaultActiveUpgrade1 = new ClickUpgrade("ua_1", "nombremejora1", 9, "", "Active");
            ClickUpgrade defaultActiveUpgrade2 = new ClickUpgrade("ua_2", "nombremejora2", 9, "", "Active");

            ClickUpgrade defaultPassiveUpgrade1 = new ClickUpgrade("up_1", "nombremejora1", 9, "", "Passive");
            ClickUpgrade defaultPassiveUpgrade2 = new ClickUpgrade("up_2", "nombremejora2", 9, "", "Passive");

            Level defaultLevel1_1 = new Level("level_1", "ua_1", "1", 2, 4);
            Level defaultLevel1_2 = new Level("level_2", "ua_2", "2", 6, 8);

            Level defaultLevel2_1 = new Level("level_1", "up_1", "1", 2, 4);
            Level defaultLevel2_2 = new Level("level_2", "up_2", "2", 2, 4);


            clickUpgradeDAO.insert(defaultActiveUpgrade1);
            Log.d("Clicker-> ", "Inserting: " + defaultActiveUpgrade1);
            clickUpgradeDAO.insert(defaultActiveUpgrade2);
            clickUpgradeDAO.insert(defaultPassiveUpgrade1);
            clickUpgradeDAO.insert(defaultPassiveUpgrade2);

            levelDAO.insert(defaultLevel1_1);
            Log.d("Clicker-> ", "Inserting: " + defaultLevel1_1);
            levelDAO.insert(defaultLevel1_2);
            levelDAO.insert(defaultLevel2_1);
            levelDAO.insert(defaultLevel2_2);


        });
    }

    public LiveData<Level> getLevelForUpgradeByUserLevel(@NotNull String idUpgrade, String level) {
            return levelDAO.getLevelForUpgradeByLevel(idUpgrade, level);
    }
}

