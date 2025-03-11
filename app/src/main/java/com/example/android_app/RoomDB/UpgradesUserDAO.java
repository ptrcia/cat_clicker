package com.example.android_app.RoomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UpgradesUserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UpgradesUser upgradesUser);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UpgradesUser>upgradesUser);

    //consultar el nivel de la mejora idUpgrade
    @Query("SELECT userLevel FROM user_upgrades WHERE idUpgrades = :idUpgrade")
    String getUserLevel(String idUpgrade);

    @Query("UPDATE user_upgrades SET userLevel = :level WHERE idUpgrades = :idUpgrade")
    void updateUserLevel(String idUpgrade, String level);

    @Query("UPDATE user_upgrades SET userLevel = 0")
    void resetUserUpgrades();
}
