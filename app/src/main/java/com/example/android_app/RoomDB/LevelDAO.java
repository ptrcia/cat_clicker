package com.example.android_app.RoomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface LevelDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Level level);

    @Query("SELECT * FROM level_table WHERE idUpgrade = :idUpgrade")
    LiveData<List<Level>> getLevelsForUpgrade(String idUpgrade);

    @Query("SELECT * FROM level_table WHERE idUpgrade = :idUpgrade AND idLevel = :level")
    LiveData<Level> getLevelForUpgradeByLevel(@NotNull String idUpgrade, String level);
}
