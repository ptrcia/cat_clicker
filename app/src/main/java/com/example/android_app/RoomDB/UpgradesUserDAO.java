package com.example.android_app.RoomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UpgradesUserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UpgradesUser upgradesUser);

    @Query("SELECT * FROM user_upgrades WHERE id = :id LIMIT 1")
    LiveData<UpgradesUser> getUpgradesByUserId(int id);
}
