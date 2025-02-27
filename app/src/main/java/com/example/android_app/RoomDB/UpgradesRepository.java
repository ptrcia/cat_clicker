package com.example.android_app.RoomDB;
import android.app.Application;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.List;

import javax.security.auth.callback.Callback;

public class UpgradesRepository {
    private final ClickUpgradeDAO clickUpgradeDAO;
    private final LevelDAO levelDAO;
    private final Executor executorService;


    public UpgradesRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        clickUpgradeDAO = db.clickUpgradeDAO();
        levelDAO = db.levelDAO();
        executorService = AppDataBase.databaseWriteExecutor;

        executorService.execute(() -> {
            List<ClickUpgrade> allUpgrades = clickUpgradeDAO.getAllUpgrades();
            if (allUpgrades == null || allUpgrades.isEmpty()) {
                Log.d("Clicker-> ", "Table is empty");
                upgradesTable();
            } else {
                Log.d("Clicker-> ", "Table is not empty");
            }
        });

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
    public ClickUpgrade getClickUpgradeById(String id) {
        return clickUpgradeDAO.getClickUpgradeById(id);
    }

    //UPgrades por tipo
    public void getClickUpgradeByType(String type, BaseCallback<List<ClickUpgrade>> callback) {
        executorService.execute(() -> {
            callback.onSuccess(clickUpgradeDAO.getClickUpgradeByType(type));
        });
    }


    public void getLevelForUpgradeByUserLevel(@NotNull String idUpgrade, String level, BaseCallback<Level> callback) {
        executorService.execute(()-> {
            callback.onSuccess(levelDAO.getLevelForUpgradeByLevel(idUpgrade, level));
        });
    }

    public void upgradesTable() {

        executorService.execute(() -> {
            ClickUpgrade defaultActiveUpgrade1 = new ClickUpgrade("ua_1", "nombremejora1", 9, "", "Active");
            ClickUpgrade defaultActiveUpgrade2 = new ClickUpgrade("ua_2", "nombremejora2", 9, "", "Active");

            ClickUpgrade defaultPassiveUpgrade1 = new ClickUpgrade("up_1", "nombremejora1", 9, "", "Passive");
            ClickUpgrade defaultPassiveUpgrade2 = new ClickUpgrade("up_2", "nombremejora2", 9, "", "Passive");

            Level defaultLevel1_1 = new Level("level1_1", "ua_1", "1", 2, 4);
            Level defaultLevel1_2 = new Level("level1_2", "ua_2", "2", 6, 8);

            Level defaultLevel2_1 = new Level("level2_1", "up_1", "1", 2, 4);
            Level defaultLevel2_2 = new Level("level2_2", "up_2", "2", 6, 8);


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


}

