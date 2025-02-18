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

    @Query("SELECT * FROM click_upgrade WHERE `key` = 'acp_1'")
    LiveData<List<ClickUpgrade>> getActiveUpgrade1();
    @Query("SELECT * FROM click_upgrade WHERE `key` = 'acp_2'")
    LiveData<List<ClickUpgrade>> getActiveUpgrade2();

    @Query("SELECT * FROM click_upgrade WHERE `key` = '9cp_1'")
    LiveData<List<ClickUpgrade>> getPassiveUpgrade1();
    @Query("SELECT * FROM click_upgrade WHERE `key` = '9cp_2'")
    LiveData<List<ClickUpgrade>> getPassiveUpgrade2();
}
