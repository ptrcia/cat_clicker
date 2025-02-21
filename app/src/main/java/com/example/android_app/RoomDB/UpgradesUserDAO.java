package com.example.android_app.RoomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UpgradesUserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UpgradesUser upgradesUser);

    @Query("SELECT * FROM user_upgrades WHERE id = :id LIMIT 1")
    LiveData<UpgradesUser> getUpgradesByUserId(String id);

    //consultar el nivel de la mejora+
    @Query("SELECT userLevel FROM user_upgrades WHERE idUser = :idUser")
    LiveData<String> getUserLevel(String idUser);

    //consultar una mejora concreta a un niuvel concreto
    @Query("SELECT idUpgrades FROM user_upgrades WHERE idUser = :idUser AND userLevel = :userLevel")
    LiveData<String> getUserUpgrade(String idUser, int userLevel);

    @Query("SELECT * FROM user_upgrades WHERE idUser = :userId")
    LiveData<List<UpgradesUser>> getAllUserUpgrades(String userId);

    //selecionar  tpdp spbre una mejora concreta de un usuario concreto
    @Query("SELECT * FROM user_upgrades WHERE idUser = :userId AND idUpgrades = :upgradeId")
    LiveData<UpgradesUser> getUserUpgrade(String userId, String upgradeId);

    @Query("SELECT userLevel FROM user_upgrades WHERE idUser = :userId AND idUpgrades = :upgradeId")
    LiveData<String> getUserUpgradeLevel(String userId, String upgradeId);

}
