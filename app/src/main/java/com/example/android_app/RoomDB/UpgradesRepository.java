package com.example.android_app.RoomDB;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
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

        upgradesTable();
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
        return clickUpgradeDAO.getClickUpgradeByType(type);
    }
    //get all upgrades
    public LiveData<List<ClickUpgrade>> getAllUpgrades() {
        return clickUpgradeDAO.getAllUpgrades();
    }


    public void upgradesTable() {
        executorService.execute(() -> {
            ClickUpgrade defaultActiveUpgrade1 = new ClickUpgrade(1, "nombremejora1", 1, "", "Active");
            ClickUpgrade defaultActiveUpgrade2 = new ClickUpgrade(2, "nombremejora2", 1, "", "Active");

            ClickUpgrade defaultPassiveUpgrade1 = new ClickUpgrade(1, "nombremejora1", 1, "", "Passive");
            ClickUpgrade defaultPassiveUpgrade2 = new ClickUpgrade(2, "nombremejora2", 1, "", "Passive");

            Level defaultLevel1_1 = new Level(1, 1, 101, 2, 4);
            Level defaultLevel1_2 = new Level(2, 1, 101, 6, 8);

            Level defaultLevel2_1 = new Level(1, 2, 101, 2, 4);
            Level defaultLevel2_2 = new Level(2, 2, 101, 2, 4);

            levelDAO.insert(defaultLevel1_1);
            Log.d("Click-> ", "Inserting: " + defaultLevel1_1);
            levelDAO.insert(defaultLevel1_2);
            levelDAO.insert(defaultLevel2_1);
            levelDAO.insert(defaultLevel2_2);

            clickUpgradeDAO.insert(defaultActiveUpgrade1);
            Log.d("Click-> ", "Inserting: " + defaultActiveUpgrade1);
            clickUpgradeDAO.insert(defaultActiveUpgrade2);
            clickUpgradeDAO.insert(defaultPassiveUpgrade1);
            clickUpgradeDAO.insert(defaultPassiveUpgrade2);
        });
    }
}

