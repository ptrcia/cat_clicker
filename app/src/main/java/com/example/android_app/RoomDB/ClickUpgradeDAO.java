package com.example.android_app.RoomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClickUpgradeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ClickUpgrade clickUpgrade);

    @Query("SELECT * FROM click_upgrade")
    LiveData<List<ClickUpgrade>> getAllUpgrades();

    @Query("SELECT * FROM click_upgrade WHERE 'id' = :id")
    LiveData<ClickUpgrade> getClickUpgradeById(String id);

    @Query("SELECT * FROM click_upgrade WHERE type = :type")
    LiveData<List<ClickUpgrade>> getClickUpgradeByType(String type);

    @Query("SELECT * FROM click_upgrade WHERE type = 'active'")
    LiveData<List<ClickUpgrade>> getActiveUpgrades();

    @Query("SELECT * FROM click_upgrade WHERE type = 'passive'")
    LiveData<List<ClickUpgrade>> getPassiveUpgrades();
}