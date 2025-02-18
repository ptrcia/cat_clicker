package com.example.android_app.RoomDB;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

public class QueryTest {
    private AppDataBase db;
    private LifecycleOwner lifecycleOwner;


    public QueryTest(AppDataBase db, LifecycleOwner lifecycleOwner){
        this.db = db;
        this.lifecycleOwner = lifecycleOwner;

    }

    public void userStatsQuery() {

        //Consulta 1
        LiveData<List<UserStats>> userStatsLiveData = db.userStatsDAO().getAllUserStats();
        userStatsLiveData.observe(lifecycleOwner, new Observer<List<UserStats>>() {
            @Override
            public void onChanged(List<UserStats> userStats) {
                for (UserStats userStat : userStats) {
                    //Solo hay 1 así que solo imprime 1
                    Log.d("QueryExample", "User: " + userStat.getName() + ", Total Score: " + userStat.getTotalScore());
                }
            }
        });
    }
    public void clickUpgradeQuery(){
        //Consulta 2
        LiveData<List<ClickUpgrade>> clickUpgradesLiveData = db.clickUpgradeDAO().getAllUpgrades();
        clickUpgradesLiveData.observe(lifecycleOwner, new Observer<List<ClickUpgrade>>() {
            @Override
            public void onChanged(List<ClickUpgrade> clickUpgrades) {
                for (ClickUpgrade clickUpgrade : clickUpgrades) {
                    Log.d("QueryExample", "Upgrade: " + clickUpgrade.getName() + ", Key: " + clickUpgrade.getKey() + ", Description: " + clickUpgrade.getDescription());
                    List<Level> levels = clickUpgrade.getLevel();
                    if (levels != null) {
                        for (Level level : levels) {
                            Log.d("QueryExample", "Level: " + level.getLevel() + ", Cost: " + level.getCost() + ", Effect: " + level.getEffect());
                        }
                    } else {
                        Log.d("QueryExample", "Levels: null");
                    }
                }
            }
        });

    }

    //Lista ClickUpgrades
    public LiveData<List<ClickUpgrade>> getAllClickUpgrades() {
        return db.clickUpgradeDAO().getAllUpgrades();
    }

    //Lista ClickUpgrades activos
    public LiveData<ClickUpgrade> getAllActiveUpgradesL1() {
        return db.clickUpgradeDAO().getActiveUpgrade1();
    }
  /*  public  LiveData<ClickUpgrade> gerActiveUpgrade1Level1() {
        return db.clickUpgradeDAO().getActiveUpgrade1Level1();
    }
    public LiveData<ClickUpgrade> getAllActiveUpgradesL2Level1() {
        return db.clickUpgradeDAO().getActiveUpgrade2Level1();
    }*/



    //Lista ClickUpgrades pasivos
    public LiveData<ClickUpgrade> getAllPassiveUpgradesL1() {
        return db.clickUpgradeDAO().getPassiveUpgrade1();
    }

    public LiveData<ClickUpgrade> getAllPassiveUpgradesL2() {
        return db.clickUpgradeDAO().getPassiveUpgrade2();
    }

    //Lista UserStats
    public LiveData<List<UserStats>> getAllUserStats() {
        return db.userStatsDAO().getAllUserStats();
    }

    //objetner los nivele s(mejoras) de un usuario
    public LiveData<List<UpgradesUser>> getUserActiveLevels(String user) {
        return Transformations.map(db.userStatsDAO().getAllUserStats(), userStatsList -> {
            List<UpgradesUser> activeLevels = new ArrayList<>();
            for (UserStats userStats : userStatsList) {
                activeLevels.addAll(userStats.getLevelActive());
            }
            return activeLevels;
        });
    }
    public LiveData<UserStats> getUserStats(String user) {
        return db.userStatsDAO().getUserStatsByName(user);
    }
}
