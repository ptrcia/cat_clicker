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

                            databaseWriteExecutor.execute(() -> initData(context)); //metodo para iniciar datos
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


        List<ClickUpgrade> activeUpgrades = new ArrayList<>();
        List<ClickUpgrade> passiveUpgrades = new ArrayList<>();

        for (int i = 1; i <= 18; i++) {
            activeUpgrades.add(new ClickUpgrade("ua_" + i, "Mejora" + i, 0, "", "Active"));
            passiveUpgrades.add(new ClickUpgrade("up_" + i, "Mejora" + i, 0, "", "Passive"));
        }

        Log.d("Clicker->", "Mejoras activas: " + activeUpgrades);
        clickUpgradeDAO.insertAll(activeUpgrades);
        clickUpgradeDAO.insertAll(passiveUpgrades);

        //Cálculo de coste y efecto exponencial

        double baseCost = 50; //coste de base
        double growthRate = 2.5; //crecimiento exponencial
        double upgradeMultiplier = 100; //cuanto vale cada nivel dentro de cada mejora, por ejemplo del nivel 1 al nivel 2
        double effectMultiplier = 0.05; //el efecto asociado al coste, 0.4 es un 40%

        double baseCostP = 5;//1500; //coste de base
        double growthRateP = 2.3; //crecimiento exponencial
        double upgradeMultiplierP = 75; //cuanto vale cada nivel dentro de cada mejora, por ejemplo del nivel 1 al nivel 2
        double effectMultiplierP = 0.03; //el efecto asociado al coste, 0.4 es un 40%

        double minEffect = 0.2;

//Niveles de las mejoras
        List<Level> levels = new ArrayList<>();

        for (int i = 1; i <= 18; i++) { // mejora
            for (int j = 1; j <= 3; j++) { //  nivel

                double cost = baseCost * Math.pow(growthRate, (i - 1)) * Math.pow(upgradeMultiplier, (j - 1));
                double effectMultiplierA = effectMultiplier+ (i * 0.005);
                double effect = Math.max(cost* effectMultiplierA, minEffect);
                levels.add(new Level("levelA" + i + "_" + j, "ua_" + i, String.valueOf(j), cost,  effect));


                double costPassive = baseCostP * Math.pow(growthRateP, (i - 1)) * Math.pow(upgradeMultiplierP, (j - 1));
                double effectMultiplierPassive = effectMultiplierP + (i * 0.003);
                double effectPassive = Math.max(costPassive * effectMultiplierPassive, minEffect);
                levels.add(new Level("levelP" + i + "_" + j, "up_" + i, String.valueOf(j),  costPassive,  effectPassive));
            }
        }
        Log.d("Clicker->", "Niveles: " + levels);
        levelDAO.insertAll(levels);

        // Insertar al usuariuo
        UserStats userStats = new UserStats("User1", "User1", 0, 0, 1);
        Log.d("Clicker->", "Usuario: " + userStats);
        userStatsDAO.insert(userStats);


        List<UpgradesUser> upgradesUsers = new ArrayList<>();

        for (int i = 1; i <= 18; i++) {
            upgradesUsers.add(new UpgradesUser("upgradeuserActive_" + i, "ua_" + i, "0", "User1"));
            upgradesUsers.add(new UpgradesUser("upgradeuserPassive_" + i, "up_" + i, "0", "User1"));
        }
        Log.d("Clicker->", "Mejoras usuario: " + upgradesUsers);
        upgradesUserDAO.insertAll(upgradesUsers);

    }
}
