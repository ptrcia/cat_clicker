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
    public void clickUpgradeQuery() {
    }

    //Lista ClickUpgrades
    public LiveData<List<ClickUpgrade>> getAllClickUpgrades() {
        return db.clickUpgradeDAO().getAllUpgrades();
    }

    //Lista ClickUpgrades activos
    public LiveData<ClickUpgrade> getAllActiveUpgradesL1() {
        return db.clickUpgradeDAO().getActiveUpgrade1();
    }

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

    public LiveData<UserStats> getUserStats(String user) {
        return db.userStatsDAO().getUserStatsByName(user);
    }
}
