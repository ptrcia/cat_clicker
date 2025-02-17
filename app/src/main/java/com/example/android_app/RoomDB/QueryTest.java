package com.example.android_app.RoomDB;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class QueryTest {
    private AppDataBase db;
    private LifecycleOwner lifecycleOwner;


    public QueryTest(AppDataBase db, LifecycleOwner lifecycleOwner){
        this.db = db;
        this.lifecycleOwner = lifecycleOwner;

    }

    public void executeQuery() {

        //Consulta 1
        LiveData<List<UserStats>> userStatsLiveData = db.userStatsDAO().getAllUserStats();
            userStatsLiveData.observe(lifecycleOwner, new Observer<List<UserStats>>() {
                @Override
                public void onChanged(List<UserStats> userStats) {
                    for (UserStats userStat : userStats) {
                        //Solo hay 1 así que solo imprime 1
                        Log.d("QueryExample", "User: " + userStat.getName() + ", Total Score: " + userStat.GetTotalScore());
                    }
                }
        });
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
}
