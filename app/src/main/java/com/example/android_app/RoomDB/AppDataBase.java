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
                            Log.d("Clicker->", "database abierta");
                        }
                    })


                    //hasta build es para que salga por consola
                    .setJournalMode(JournalMode.TRUNCATE)
                    .setQueryCallback((sqlQuery, bindArgs) -> {
                        Log.d("RoomQuery", "SQL Query: " + sqlQuery + ", Bind Args: " + bindArgs);
                    }, Executors.newSingleThreadExecutor())
                    //.fallbackToDestructiveMigration()
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

    }
}
