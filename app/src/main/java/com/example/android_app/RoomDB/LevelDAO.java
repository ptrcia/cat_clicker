package com.example.android_app.RoomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LevelDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Level level);

    @Query("SELECT * FROM level WHERE clickUpgradeId = :clickUpgradeId ORDER BY level ASC")
    LiveData<List<Level>> getLevelsByClickUpgrade(int clickUpgradeId);
}
