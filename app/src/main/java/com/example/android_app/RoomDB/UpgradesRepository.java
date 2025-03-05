package com.example.android_app.RoomDB;
import android.app.Application;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        //db.clearAllTables(); /// TENER EN CUENTA ESTO PARA LA BUILD FINAL
        executorService.execute(() -> {
            List<ClickUpgrade> allUpgrades = clickUpgradeDAO.getAllUpgrades();
            if (allUpgrades == null || allUpgrades.isEmpty()) {
                Log.d("Clicker-> ", "Table is empty");
                //upgradesTable();
            } else {
                Log.d("Clicker-> ", "Table is not empty");
                //upgradesTable(); ///// TENER EN CUENTA ESTO PARA LA BUILD FINAL
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

            ClickUpgrade defaultActiveUpgrade1 = new ClickUpgrade("ua_1", "activa1", 0, "hola", "Active");
            ClickUpgrade defaultActiveUpgrade2 = new ClickUpgrade("ua_2", "activa2", 0, "adiós", "Active");


            ClickUpgrade defaultPassiveUpgrade1 = new ClickUpgrade("up_1", "pasiva1", 0, "", "Passive");
            ClickUpgrade defaultPassiveUpgrade2 = new ClickUpgrade("up_2", "pasiva2", 0, "", "Passive");

            Level defaultLevelActive1_1 = new Level("levelActive1_1", "ua_1", "1", 2, 1);
            Level defaultLevelActive1_2 = new Level("levelActive1_2", "ua_1", "2", 6, 2);
            Level defaultLevelActive1_3 = new Level("levelActive1_3", "ua_1", "3", 12, 3);


            Level defaultLevelActive2_1 = new Level("levelActive2_1", "ua_2", "1", 10, 5);
            Level defaultLevelActive2_2 = new Level("levelActive2_2", "ua_2", "2", 20, 8);
            Level defaultLevelActive2_3 = new Level("levelActive2_3", "ua_2", "3", 40, 10);

            Level defaultLevelPassive1_1 = new Level("levelPassive1_1", "up_1", "1", 2, 1);
            Level defaultLevelPassive1_2 = new Level("levelPassive1_2", "up_1", "2", 15, 8);
            Level defaultLevelPassive1_3 = new Level("levelPassive1_3", "up_1", "3", 26, 10);
            Level defaultLevelPassive2_1 = new Level("levelPassive2_1", "up_2", "1", 50, 2);
            Level defaultLevelPassive2_2 = new Level("levelPassive2_2", "up_2", "2", 60, 8);
            Level defaultLevelPassive2_3 = new Level("levelPassive2_3", "up_2", "3", 80,14);


            clickUpgradeDAO.insert(defaultActiveUpgrade1);
            Log.d("Clicker-> ", "Inserting: " + defaultActiveUpgrade1.getId());
            clickUpgradeDAO.insert(defaultActiveUpgrade2);


            clickUpgradeDAO.insert(defaultPassiveUpgrade1);
            clickUpgradeDAO.insert(defaultPassiveUpgrade2);

            levelDAO.insert(defaultLevelActive1_1);
            Log.d("Clicker-> ", "Inserting: " + defaultLevelActive1_1.getId());
            levelDAO.insert(defaultLevelActive1_2);
            levelDAO.insert(defaultLevelActive1_3);
            levelDAO.insert(defaultLevelActive2_1);
            levelDAO.insert(defaultLevelActive2_2);
            levelDAO.insert(defaultLevelActive2_3);
            levelDAO.insert(defaultLevelPassive1_1);
            levelDAO.insert(defaultLevelPassive1_2);
            levelDAO.insert(defaultLevelPassive1_3);
            levelDAO.insert(defaultLevelPassive2_1);
            levelDAO.insert(defaultLevelPassive2_2);
//            Log.d("Clicker-> ", "Inserting: " + defaultLevelPassive2_2.getId());
            levelDAO.insert(defaultLevelPassive2_3);

            /*

            //Mejoras activas y pasivas
            List<ClickUpgrade> activeUpgrades = new ArrayList<>();
            List<ClickUpgrade> passiveUpgrades = new ArrayList<>();

            for (int i = 1; i <= 5; i++) {
                activeUpgrades.add(new ClickUpgrade("ua_" + i, "nombremejora" + i, 0, "", "Active"));
                passiveUpgrades.add(new ClickUpgrade("up_" + i, "nombremejora" + i, 0, "", "Passive"));
            }
//Añadirlas
            for (ClickUpgrade upgrade : activeUpgrades) {
                clickUpgradeDAO.insert(upgrade);
            }
            for (ClickUpgrade upgrade : passiveUpgrades) {
                clickUpgradeDAO.insert(upgrade);
            }


            //Cálculo de coste y efecto exponencial

            double baseCost = 10;
            double growthRate = 10;
            double upgradeMultiplier = 6;
            double effectMultiplier = 0.4;

//Niveles de las mejoras
            List<Level> levels = new ArrayList<>();

            for (int i = 1; i <= 5; i++) { // mejora
                for (int j = 1; j <= 3; j++) { //  nivel
                    double cost = baseCost * Math.pow(growthRate, (i - 1)) * Math.pow(upgradeMultiplier, (j - 1));
                    double effect = cost * effectMultiplier;

                    levels.add(new Level("levelA" + i + "_" + j, "ua_" + i, String.valueOf(j), (int)cost, (int) effect));
                    levels.add(new Level("levelP" + i + "_" + j, "up_" + i, String.valueOf(j), (int) cost, (int) effect));
                }
            }
//añadirlas
            for (Level level : levels) {
                levelDAO.insert(level);
            }

             */

        });
    }


}

