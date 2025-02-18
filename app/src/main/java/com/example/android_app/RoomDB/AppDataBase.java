package com.example.android_app.RoomDB;

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

@Database(entities = {ClickUpgrade.class, UserStats.class, Level.class, UpgradesUser.class}, version =2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    //region Configuración

    //DAOs
    public abstract ClickUpgradeDAO clickUpgradeDAO();
    public abstract UserStatsDAO userStatsDAO();
    public abstract LevelDao levelDAO();
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
                    .addCallback(databaseCallback)
                    //hasta build es para que salga por consola
                    .setJournalMode(JournalMode.TRUNCATE)
                    .setQueryCallback((sqlQuery, bindArgs) -> {
                        Log.d("RoomQuery", "SQL Query: " + sqlQuery + ", Bind Args: " + bindArgs);
                    }, Executors.newSingleThreadExecutor())
                    .fallbackToDestructiveMigration()
                    .build();
                }
        return instance;
    }

    //endregion
    //region Tablas
    //Inicializar las tablas de mejoras y de niveles aqui porque son estáticas

    private static final RoomDatabase.Callback databaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NotNull SupportSQLiteDatabase db){
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{
                // instancia de la base de datos
                AppDataBase dataBase = instance;
                if (dataBase == null) return;
                ClickUpgradeDAO clickUpgradeDAO = dataBase.clickUpgradeDAO(); //DAO que usar
                UserStatsDAO userStatsDAO = dataBase.userStatsDAO();

                //Insertar UserStats vacio porq va a leerse y escribirse

                List<UpgradesUser> levelsUserActive = new ArrayList<>();
                levelsUserActive.add(new UpgradesUser( 0, 0, 1));
                levelsUserActive.add(new UpgradesUser( 0, 0, 1));

                List<UpgradesUser> levelsUserPassive = new ArrayList<>();
                levelsUserPassive.add(new UpgradesUser( 0, 0, 1));
                levelsUserPassive.add(new UpgradesUser(  0,0, 1));

                UserStats userStats = new UserStats("User1", 0, 0, 0, 1 );

                userStatsDAO.insert(userStats);

                // niveles dentro de cada mejora activa


                //Llistas de mejoras activas y pasivas
                clickUpgradeDAO.insert(new ClickUpgrade(1, "Cuchue", "","Activa"));
                clickUpgradeDAO.insert(new ClickUpgrade(2, "Castillo", "","Activa"));

                clickUpgradeDAO.insert(new ClickUpgrade(1, "Gato", "","Pasiva"));
                clickUpgradeDAO.insert(new ClickUpgrade(2, "Veterinario",  "","Pasiva"));

            });
        }
    };
    //endregion
}
