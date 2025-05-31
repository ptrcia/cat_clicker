package com.patriciagracia.catclicker.RoomDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.patriciagracia.catclicker.Game;

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
    boolean mode66= false;

    static double growthRate=3;
    static double growthRateP=8;


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
    public boolean getmode66(){return mode66;}
    public void mode66(Context context){
        Game.getInstance().setAreAllActivePurchased(false);
        Game.getInstance().setAreAllPassivePurchased(false);
        numberOfUpgrades=66;
        numberOfLevels=66;
        growthRate = 10;
        growthRateP = 15;
        databaseWriteExecutor.execute(() ->  initData(userRepositoryContext));
        mode66 = true;
        Savemode66Preference(context);
    }

    private void Savemode66Preference(Context context) {
        sharedPreferences = context.getSharedPreferences("mode66", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("mode66", true);
        editor.apply();
    }
    public boolean loadmode66Preference(Context context) {
        if (context == null) {
            Log.e("mode66", "El contexto es nulo");
            return false;
        }
        sharedPreferences = context.getSharedPreferences("mode66", context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("mode66", false); // false como valor predeterminado
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

        //Cálculo de coste y efecto exponencial

        double baseCost = 500; // coste inicial
        //growthRate = 10; // crecimietno entre mejoras
        double upgradeMultiplier = 2.0; // crecimiento entre niveles de una misma mejora
        double effectMultiplier = 0.0004; // el efecto

        double baseCostP = 500;
        //growthRateP = 15;
        double upgradeMultiplierP = 2.2;
        double effectMultiplierP = 0.0003;

        double minEffect = 0.01;

// Niveles de las mejoras
        List<Level> levels = new ArrayList<>();

        for (int i = 1; i <= numberOfUpgrades; i++) { // mejora
            for (int j = 1; j <= numberOfLevels; j++) { // nivel

                double cost = baseCost * Math.pow(growthRate, i - 1) * Math.pow(upgradeMultiplier, j - 1);
                double costPassive = baseCostP * Math.pow(growthRateP, i - 1) * Math.pow(upgradeMultiplierP, j - 1);

                boolean testMode = false;
                if (testMode) {
                    cost = 1;
                    costPassive = 1;
                }

                // Aumentamos ligeramente el efecto por mejora (pero sigue siendo pequeño al inicio)
                double effectMultiplierA = effectMultiplier + (i * 0.00005);
                double effect = Math.max(cost * effectMultiplierA, minEffect);
                levels.add(new Level("levelA" + i + "_" + j, "ua_" + i, String.valueOf(j), cost, effect));

                double effectMultiplierPassive = effectMultiplierP + (i * 0.00003);
                double effectPassive = Math.max(costPassive * effectMultiplierPassive, minEffect);
                levels.add(new Level("levelP" + i + "_" + j, "up_" + i, String.valueOf(j), costPassive, effectPassive));
            }
        }


        Log.d("Clicker->", "Niveles: " + levels);
        levelDAO.insertAll(levels);


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
