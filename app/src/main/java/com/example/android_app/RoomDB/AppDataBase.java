package com.example.android_app.RoomDB;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ClickUpgrade.class, UserStats.class, Level.class, UpgradesUser.class}, version =4, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    //region Configuración
    //DAOs
    public abstract ClickUpgradeDAO clickUpgradeDAO();
    public abstract UserStatsDAO userStatsDAO();
    public abstract LevelDAO levelDAO();
    public abstract UpgradesUserDAO upgradesUserDAO();

    //Manejo de hilos
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    //Configurar la database
    private static volatile AppDataBase instance;

    public static AppDataBase getDatabase(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "app_database")
                    //.fallbackToDestructiveMigration()
                    .addCallback(new RoomDatabase.Callback(){
                        @Override
                        public void onCreate(@NotNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Log.d("Clicker->", " database creada.");

                            databaseWriteExecutor.execute(() -> initData2(context)); //metodo para iniciar datos
                        }
                        @Override
                        public void onOpen(@NotNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                            //databaseWriteExecutor.execute(() -> initData2(context));
                            Log.d("Clicker->", "database abierta");
                        }
                    })


                    //hasta build es para que salga por consola
                    .setJournalMode(JournalMode.TRUNCATE)
                    .setQueryCallback((sqlQuery, bindArgs) -> {
                        Log.d("RoomQuery", "SQL Query: " + sqlQuery + ", Bind Args: " + bindArgs);
                    }, Executors.newSingleThreadExecutor())

                    .build();
                }
        return instance;
    }
    //endregion

    private static void initData(Context context) {
        AppDataBase db = getDatabase(context);
        ClickUpgradeDAO clickUpgradeDAO = db.clickUpgradeDAO();
        LevelDAO levelDAO = db.levelDAO();
        UpgradesUserDAO upgradesUserDAO = db.upgradesUserDAO();
        UserStatsDAO userStatsDAO = db.userStatsDAO();

        //Datos del usuario
        UserStats userStats = new UserStats("User1", "User1", 0, 0, 1);
        userStatsDAO.insert(userStats);

        //Datos sobre las mejoras que tiene adquiridas el usuario.
        UpgradesUser upgradesUserActive1 = new UpgradesUser("upgradeuserActive_1", "ua_1", "0", "User1");
        upgradesUserDAO.insert(upgradesUserActive1);
        UpgradesUser upgradesUserActive2 = new UpgradesUser("upgradeuserActive_2", "ua_2", "0", "User1");
        upgradesUserDAO.insert(upgradesUserActive2);
        UpgradesUser upgradesUserActive3 = new UpgradesUser("upgradeuserActive_3", "ua_3", "0", "User1");
        upgradesUserDAO.insert(upgradesUserActive3);


        UpgradesUser upgradesUserPassive1 = new UpgradesUser("upgradeuserPassive_1", "up_1", "0", "User1");
        upgradesUserDAO.insert(upgradesUserPassive1);
        UpgradesUser upgradesUserPassive2 = new UpgradesUser("upgradeuserPassive_2", "up_2", "0", "User1");
        upgradesUserDAO.insert(upgradesUserPassive2);
        UpgradesUser upgradesUserPassive3 = new UpgradesUser("upgradeuserPassive_3", "up_3", "0", "User1");
        upgradesUserDAO.insert(upgradesUserPassive3);


        ClickUpgrade defaultActiveUpgrade1 = new ClickUpgrade("ua_1", "activa1", 0, "hola", "Active");
        ClickUpgrade defaultActiveUpgrade2 = new ClickUpgrade("ua_2", "activa2", 0, "adiós", "Active");
        ClickUpgrade defaultActiveUpgrade3 = new ClickUpgrade("ua_3", "activa3", 0, "buenos días", "Active");

        ClickUpgrade defaultPassiveUpgrade1 = new ClickUpgrade("up_1", "pasiva1", 0, "", "Passive");
        ClickUpgrade defaultPassiveUpgrade2 = new ClickUpgrade("up_2", "pasiva2", 0, "", "Passive");
        ClickUpgrade defaultPassiveUpgrade3 = new ClickUpgrade("up_3", "pasiva3", 0, "", "Passive");

        Level defaultLevelActive1_1 = new Level("levelActive1_1", "ua_1", "1", 2, 1);
        Level defaultLevelActive1_2 = new Level("levelActive1_2", "ua_1", "2", 6, 2);
        Level defaultLevelActive1_3 = new Level("levelActive1_3", "ua_1", "3", 12, 3);


        Level defaultLevelActive2_1 = new Level("levelActive2_1", "ua_2", "1", 10, 5);
        Level defaultLevelActive2_2 = new Level("levelActive2_2", "ua_2", "2", 20, 8);
        Level defaultLevelActive2_3 = new Level("levelActive2_3", "ua_2", "3", 40, 10);

        Level defaultLevelActive3_1 = new Level("levelActive3_1", "ua_3", "1", 100, 50);
        Level defaultLevelActive3_2 = new Level("levelActive3_2", "ua_3", "2", 200, 80);
        Level defaultLevelActive3_3 = new Level("levelActive3_3", "ua_3", "3", 400, 100);

        Level defaultLevelPassive1_1 = new Level("levelPassive1_1", "up_1", "1", 2, 1);
        Level defaultLevelPassive1_2 = new Level("levelPassive1_2", "up_1", "2", 15, 8);
        Level defaultLevelPassive1_3 = new Level("levelPassive1_3", "up_1", "3", 26, 10);

        Level defaultLevelPassive2_1 = new Level("levelPassive2_1", "up_2", "1", 50, 2);
        Level defaultLevelPassive2_2 = new Level("levelPassive2_2", "up_2", "2", 60, 8);
        Level defaultLevelPassive2_3 = new Level("levelPassive2_3", "up_2", "3", 80,14);

        Level defaultLevelPassive3_1 = new Level("levelPassive3_1", "up_3", "1", 500, 20);
        Level defaultLevelPassive3_2 = new Level("levelPassive3_2", "up_3", "2", 600, 80);
        Level defaultLevelPassive3_3 = new Level("levelPassive3_3", "up_3", "3", 800, 140);


        clickUpgradeDAO.insert(defaultActiveUpgrade1);
        Log.d("Clicker-> ", "Inserting: " + defaultActiveUpgrade1.getId());
        clickUpgradeDAO.insert(defaultActiveUpgrade2);
        clickUpgradeDAO.insert(defaultActiveUpgrade3);


        clickUpgradeDAO.insert(defaultPassiveUpgrade1);
        clickUpgradeDAO.insert(defaultPassiveUpgrade2);
        clickUpgradeDAO.insert(defaultPassiveUpgrade3);

        levelDAO.insert(defaultLevelActive1_1);
        Log.d("Clicker-> ", "Inserting: " + defaultLevelActive1_1.getId());
        levelDAO.insert(defaultLevelActive1_2);
        levelDAO.insert(defaultLevelActive1_3);
        levelDAO.insert(defaultLevelActive2_1);
        levelDAO.insert(defaultLevelActive2_2);
        levelDAO.insert(defaultLevelActive2_3);
        levelDAO.insert(defaultLevelActive3_1);
        levelDAO.insert(defaultLevelActive3_2);
        levelDAO.insert(defaultLevelActive3_3);


        levelDAO.insert(defaultLevelPassive1_1);
        levelDAO.insert(defaultLevelPassive1_2);
        levelDAO.insert(defaultLevelPassive1_3);
        levelDAO.insert(defaultLevelPassive2_1);
        levelDAO.insert(defaultLevelPassive2_2);
        levelDAO.insert(defaultLevelPassive2_3);
        levelDAO.insert(defaultLevelPassive3_1);
        levelDAO.insert(defaultLevelPassive3_2);
        levelDAO.insert(defaultLevelPassive3_3);


    }
    private static void initData1(Context context) {
        AppDataBase db = getDatabase(context);
        ClickUpgradeDAO clickUpgradeDAO = db.clickUpgradeDAO();
        LevelDAO levelDAO = db.levelDAO();
        UpgradesUserDAO upgradesUserDAO = db.upgradesUserDAO();
        UserStatsDAO userStatsDAO = db.userStatsDAO();

        List<ClickUpgrade> clickUpgrades = Arrays.asList(
                new ClickUpgrade("ua_1", "activa1", 0, "hola", "Active"),
                new ClickUpgrade("ua_2", "activa2", 0, "adiós", "Active"),
                new ClickUpgrade("ua_3", "activa3", 0, "buenos días", "Active"),
                new ClickUpgrade("up_1", "pasiva1", 0, "", "Passive"),
                new ClickUpgrade("up_2", "pasiva2", 0, "", "Passive"),
                new ClickUpgrade("up_3", "pasiva3", 0, "", "Passive")
        );
        clickUpgradeDAO.insertAll(clickUpgrades);

        // Lista de niveles
        List<Level> levels = Arrays.asList(
                new Level("levelActive1_1", "ua_1", "1", 2, 1),
                new Level("levelActive1_2", "ua_1", "2", 6, 2),
                new Level("levelActive1_3", "ua_1", "3", 12, 3),
                new Level("levelActive2_1", "ua_2", "1", 10, 5),
                new Level("levelActive2_2", "ua_2", "2", 20, 8),
                new Level("levelActive2_3", "ua_2", "3", 40, 10),
                new Level("levelActive3_1", "ua_3", "1", 100, 50),
                new Level("levelActive3_2", "ua_3", "2", 200, 80),
                new Level("levelActive3_3", "ua_3", "3", 400, 100),
                new Level("levelPassive1_1", "up_1", "1", 2, 1),
                new Level("levelPassive1_2", "up_1", "2", 15, 8),
                new Level("levelPassive1_3", "up_1", "3", 26, 10),
                new Level("levelPassive2_1", "up_2", "1", 50, 2),
                new Level("levelPassive2_2", "up_2", "2", 60, 8),
                new Level("levelPassive2_3", "up_2", "3", 80, 14),
                new Level("levelPassive3_1", "up_3", "1", 500, 20),
                new Level("levelPassive3_2", "up_3", "2", 600, 80),
                new Level("levelPassive3_3", "up_3", "3", 800, 140)
        );
        levelDAO.insertAll(levels);

        // Insertar al usuariuo
        UserStats userStats = new UserStats("User1", "User1", 0, 0, 1);
        userStatsDAO.insert(userStats);

        // Lista de mejoras adquiridas por el usuario
        List<UpgradesUser> upgradesUsers = Arrays.asList(
                new UpgradesUser("upgradeuserActive_1", "ua_1", "0", "User1"),
                new UpgradesUser("upgradeuserActive_2", "ua_2", "0", "User1"),
                new UpgradesUser("upgradeuserActive_3", "ua_3", "0", "User1"),
                new UpgradesUser("upgradeuserPassive_1", "up_1", "0", "User1"),
                new UpgradesUser("upgradeuserPassive_2", "up_2", "0", "User1"),
                new UpgradesUser("upgradeuserPassive_3", "up_3", "0", "User1")
        );
        upgradesUserDAO.insertAll(upgradesUsers);

    }
    private static void initData2(Context context) {
        AppDataBase db = getDatabase(context);
        ClickUpgradeDAO clickUpgradeDAO = db.clickUpgradeDAO();
        LevelDAO levelDAO = db.levelDAO();
        UpgradesUserDAO upgradesUserDAO = db.upgradesUserDAO();
        UserStatsDAO userStatsDAO = db.userStatsDAO();


        List<ClickUpgrade> activeUpgrades = new ArrayList<>();
        List<ClickUpgrade> passiveUpgrades = new ArrayList<>();

        for (int i = 1; i <= 18; i++) {
            activeUpgrades.add(new ClickUpgrade("ua_" + i, "Mejora" + i, 0, "", "Active"));
            passiveUpgrades.add(new ClickUpgrade("up_" + i, "Mejora" + i, 0, "", "Passive"));
        }

        clickUpgradeDAO.insertAll(activeUpgrades);
        clickUpgradeDAO.insertAll(passiveUpgrades);

        //Cálculo de coste y efecto exponencial

        double baseCost = 50; //coste de base
        double growthRate = 2; //crecimiento exponencial
        double upgradeMultiplier = 50; //cuanto vale cada nivel dentro de cada mejora, por ejemplo del nivel 1 al nivel 2
        double effectMultiplier = 0.1; //el efecto asociado al coste, 0.4 es un 40%

        double baseCostP = 1;//1500; //coste de base
        double growthRateP = 2; //crecimiento exponencial
        double upgradeMultiplierP = 50; //cuanto vale cada nivel dentro de cada mejora, por ejemplo del nivel 1 al nivel 2
        double effectMultiplierP = 0.1; //el efecto asociado al coste, 0.4 es un 40%

//Niveles de las mejoras
        List<Level> levels = new ArrayList<>();

        for (int i = 1; i <= 18; i++) { // mejora
            for (int j = 1; j <= 3; j++) { //  nivel

                double cost = baseCost * Math.pow(growthRate, (i - 1)) * Math.pow(upgradeMultiplier, (j - 1));
                double effect = cost * effectMultiplier;
                levels.add(new Level("levelA" + i + "_" + j, "ua_" + i, String.valueOf(j), (int)cost, (int) effect));


                double costPassive = baseCostP * Math.pow(growthRateP, (i - 1)) * Math.pow(upgradeMultiplierP, (j - 1));
                double effectPassive = costPassive * effectMultiplierP;
                levels.add(new Level("levelP" + i + "_" + j, "up_" + i, String.valueOf(j), (int) costPassive, (int) effectPassive));
            }
        }
        levelDAO.insertAll(levels);



        // Insertar al usuariuo
        UserStats userStats = new UserStats("User1", "User1", 0, 0, 1);
        userStatsDAO.insert(userStats);

        // Lista de mejoras adquiridas por el usuario
        /*List<UpgradesUser> upgradesUsers = Arrays.asList(
                new UpgradesUser("upgradeuserActive_1", "ua_1", "0", "User1"),
                new UpgradesUser("upgradeuserActive_2", "ua_2", "0", "User1"),
                new UpgradesUser("upgradeuserActive_3", "ua_3", "0", "User1"),
                new UpgradesUser("upgradeuserActive_4", "ua_4", "0", "User1"),
                new UpgradesUser("upgradeuserActive_5", "ua_5", "0", "User1"),


                new UpgradesUser("upgradeuserPassive_1", "up_1", "0", "User1"),
                new UpgradesUser("upgradeuserPassive_2", "up_2", "0", "User1"),
                new UpgradesUser("upgradeuserPassive_3", "up_3", "0", "User1"),
                new UpgradesUser("upgradeuserPassive_4", "up_4", "0", "User1"),
                new UpgradesUser("upgradeuserPassive_5", "up_5", "0", "User1")

        );*/

        List<UpgradesUser> upgradesUsers = new ArrayList<>();

        for (int i = 1; i <= 18; i++) {
            upgradesUsers.add(new UpgradesUser("upgradeuserActive_" + i, "ua_" + i, "0", "User1"));
            upgradesUsers.add(new UpgradesUser("upgradeuserPassive_" + i, "up_" + i, "0", "User1"));
        }
        upgradesUserDAO.insertAll(upgradesUsers);

    }
}
