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

@Database(entities = {ClickUpgrade.class, UserStats.class}, version =2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {

    //region Configuración

    //DAOs
    public abstract ClickUpgradeDAO clickUpgradeDAO();
    public abstract UserStatsDAO userStatsDAO();

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
                levelsUserActive.add(new UpgradesUser( 0, 0));
                levelsUserActive.add(new UpgradesUser( 0, 0));

                List<UpgradesUser> levelsUserPassive = new ArrayList<>();
                levelsUserPassive.add(new UpgradesUser( 0, 0));
                levelsUserPassive.add(new UpgradesUser(  0,0));

                UserStats userStats = new UserStats("User1", 0, 0, 0,  levelsUserActive, levelsUserPassive);

                userStatsDAO.insert(userStats);

                //Lista de los niveles dentro de cada mejora activa
                List<Level> levelsActive1 = new ArrayList<>();
                levelsActive1.add(new Level(1, 1, 2));
                levelsActive1.add(new Level(2, 2, 4));

                List<Level> levelsActive2 = new ArrayList<>();
                levelsActive2.add(new Level(1, 4, 6));
                levelsActive2.add(new Level(2, 6, 8));

                //Lista de los niveles dentro de cada mejora pasiva
                List<Level> levelsPassive1 = new ArrayList<>();
                levelsPassive1.add(new Level(1, 1, 2));
                levelsPassive1.add(new Level(2, 2, 4));

                List<Level> levelsPassive2 = new ArrayList<>();
                levelsPassive2.add(new Level(1, 4, 6));
                levelsPassive2.add(new Level(2, 6, 8));

                //Llistas de mejoras activas y pasivas
                clickUpgradeDAO.insert(new ClickUpgrade("MejoraActiva1", "acp_1", 1,"Activa1", levelsActive1));
                clickUpgradeDAO.insert(new ClickUpgrade("MejoraActiva2", "acp_2", 1,"Activa2", levelsActive2));

                clickUpgradeDAO.insert(new ClickUpgrade("MejoraPasiva1", "pcp_1", 1,"Pasiva1", levelsPassive1));
                clickUpgradeDAO.insert(new ClickUpgrade("MejoraPasiva2", "pcp_2",  1,"Pasiva2", levelsPassive2));

            });
        }
    };
    //endregion
}
