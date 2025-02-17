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


}
