package com.example.android_app.RoomDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.android_app.Game;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    public static AppDataBase getInstance() {
        return instance;
    }

    private static  Context userRepositoryContext;
    static  int numberOfUpgrades=10; //18;
    static  int numberOfLevels=3; //3;
    boolean mode99= false;
    SharedPreferences sharedPreferences;

    public static AppDataBase getDatabase(final Context context) {
        if (instance == null) {
            userRepositoryContext = context.getApplicationContext();
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
    public boolean getMode99(){return mode99;}
    public void Mode99(Context context){
        Game.getInstance().setAreAllActivePurchased(false);
        Game.getInstance().setAreAllPassivePurchased(false);
        numberOfUpgrades=66;
        numberOfLevels=66;
        databaseWriteExecutor.execute(() ->  initData(userRepositoryContext));
        mode99 = true;
        SaveMode99Preference(context);
    }

    private void SaveMode99Preference(Context context) {
        sharedPreferences = context.getSharedPreferences("Mode99", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("mode99", true);
        editor.apply();
    }
    public boolean loadMode99Preference(Context context) {
        if (context == null) {
            Log.e("Mode99", "El contexto es nulo");
            return false;
        }
        sharedPreferences = context.getSharedPreferences("Mode99", context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("mode99", false); // false como valor predeterminado
    }



    private static void initData(Context context) {
        AppDataBase db = getDatabase(context);
        ClickUpgradeDAO clickUpgradeDAO = db.clickUpgradeDAO();
        LevelDAO levelDAO = db.levelDAO();
        UpgradesUserDAO upgradesUserDAO = db.upgradesUserDAO();
        UserStatsDAO userStatsDAO = db.userStatsDAO();


        List<ClickUpgrade> activeUpgrades = new ArrayList<>();
        List<ClickUpgrade> passiveUpgrades = new ArrayList<>();

        for (int i = 1; i <= numberOfUpgrades; i++) {
            activeUpgrades.add(new ClickUpgrade("ua_" + i, "Mejora" + i, 0, "", "Active"));
            passiveUpgrades.add(new ClickUpgrade("up_" + i, "Mejora" + i, 0, "", "Passive"));
        }

        Log.d("Clicker->", "Mejoras activas: " + activeUpgrades);
        clickUpgradeDAO.insertAll(activeUpgrades);
        clickUpgradeDAO.insertAll(passiveUpgrades);

        /*//Cálculo de coste y efecto exponencial

        double baseCost = 1000; //coste de base
        double growthRate = 12; //crecimiento exponencial
        double upgradeMultiplier = 10000; //cuanto vale cada nivel dentro de cada mejora, por ejemplo del nivel 1 al nivel 2
        double effectMultiplier = 0.002; //el efecto asociado al coste, 0.4 es un 40%

        double baseCostP = 10000;//1500; //coste de base
        double growthRateP = 15; //crecimiento exponencial
        double upgradeMultiplierP = 200; //cuanto vale cada nivel dentro de cada mejora, por ejemplo del nivel 1 al nivel 2
        double effectMultiplierP = 0.001; //el efecto asociado al coste, 0.4 es un 40%

        double minEffect = 0.01;

//Niveles de las mejoras
        List<Level> levels = new ArrayList<>();


        for (int i = 1; i <= numberOfUpgrades; i++) { // mejora
            for (int j = 1; j <= numberOfLevels; j++) { //  nivel

                double cost = baseCost * Math.pow(growthRate, i-1) * Math.pow(upgradeMultiplier, j-1);
                double costPassive = baseCostP * Math.pow(growthRateP, i-1) * Math.pow(upgradeMultiplierP, j-1);

                boolean testMode= false;
                if (testMode) {
                    cost = 1;
                    costPassive = 1;
                }

                double effectMultiplierA = effectMultiplier+ (i * 0.0001);
                double effect = Math.max(cost* effectMultiplierA, minEffect);
                levels.add(new Level("levelA" + i + "_" + j, "ua_" + i, String.valueOf(j), cost,  effect));


                double effectMultiplierPassive = effectMultiplierP + (i * 0.0005);
                double effectPassive = Math.max(costPassive * effectMultiplierPassive, minEffect);
                levels.add(new Level("levelP" + i + "_" + j, "up_" + i, String.valueOf(j),  costPassive,  effectPassive));
            }
        }
        Log.d("Clicker->", "Niveles: " + levels);
        levelDAO.insertAll(levels);*/
        // Parámetros base para el cálculo
        double baseCostActive = 100;       // Coste inicial bajo para la primera mejora activa
        double growthRateCostActive = 2.5;   // Crecimiento de coste moderado para activas
        double upgradeCostMultiplierActive = 5; // Multiplicador de coste por nivel (activas)
        double baseEffectActive = 0.02;      // Efecto inicial bajo (2%)
        double growthRateEffectActive = 1.1;  // Crecimiento de efecto lento para activas

        double baseCostPassive = 1000;      // Coste inicial más alto para la primera pasiva
        double growthRateCostPassive = 3.5;  // Crecimiento de coste más rápido para pasivas
        double upgradeCostMultiplierPassive = 10; // Multiplicador de coste por nivel (pasivas)
        double baseEffectPassive = 0.005;     // Efecto inicial muy bajo (0.5%)
        double growthRateEffectPassive = 1.2; // Crecimiento de efecto un poco más rápido para pasivas

        double minEffect = 0.01;
        boolean testMode = false;

        List<Level> levels = new ArrayList<>();

        for (int i = 1; i <= numberOfUpgrades; i++) { // Mejora
            for (int j = 1; j <= numberOfLevels; j++) { // Nivel

                // Cálculo de coste y efecto para mejoras ACTIVAS
                double costActive = baseCostActive * Math.pow(growthRateCostActive, i - 1) * Math.pow(upgradeCostMultiplierActive, j - 1);
                double effectActive = Math.max(baseEffectActive * Math.pow(growthRateEffectActive, j - 1), minEffect);

                if (testMode) {
                    costActive = 1;
                }

                levels.add(new Level("levelA" + i + "_" + j, "ua_" + i, String.valueOf(j), costActive, effectActive));

                // Cálculo de coste y efecto para mejoras PASIVAS
                double costPassive = baseCostPassive * Math.pow(growthRateCostPassive, i - 1) * Math.pow(upgradeCostMultiplierPassive, j - 1);
                double effectPassive = Math.max(baseEffectPassive * Math.pow(growthRateEffectPassive, j - 1), minEffect);

                if (testMode) {
                    costPassive = 1;
                }

                levels.add(new Level("levelP" + i + "_" + j, "up_" + i, String.valueOf(j), costPassive, effectPassive));
            }
        }
        Log.d("Clicker->", "Niveles (Nuevo Algoritmo): " + levels);
        if (levelDAO != null) {
            levelDAO.insertAll(levels);
        }

        // Insertar al usuariuo
        UserStats userStats = new UserStats("User1", "User1", 0, 0, 1);
        Log.d("Clicker->", "Usuario: " + userStats);
        userStatsDAO.insert(userStats);


        List<UpgradesUser> upgradesUsers = new ArrayList<>();

        for (int i = 1; i <= numberOfUpgrades; i++) {
            upgradesUsers.add(new UpgradesUser("upgradeuserActive_" + i, "ua_" + i, "0", "User1"));
            upgradesUsers.add(new UpgradesUser("upgradeuserPassive_" + i, "up_" + i, "0", "User1"));
        }
        Log.d("Clicker->", "Mejoras usuario: " + upgradesUsers);
        upgradesUserDAO.insertAll(upgradesUsers);

    }
}
