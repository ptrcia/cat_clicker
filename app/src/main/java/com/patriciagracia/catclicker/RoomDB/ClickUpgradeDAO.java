package com.patriciagracia.catclicker.RoomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClickUpgradeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ClickUpgrade clickUpgrade);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ClickUpgrade> clickUpgrades);

    @Query("SELECT * FROM click_upgrade")
    List<ClickUpgrade> getAllUpgrades();

    @Query("SELECT * FROM click_upgrade WHERE id = :id")
    ClickUpgrade getClickUpgradeById(String id);

    @Query("SELECT * FROM click_upgrade WHERE type = :type")
    List<ClickUpgrade> getClickUpgradeByType(String type);

    @Query("SELECT * FROM click_upgrade WHERE type = 'Active'")
    List<ClickUpgrade> getActiveUpgrades();

    @Query("SELECT * FROM click_upgrade WHERE type = 'Passive'")
    List<ClickUpgrade> getPassiveUpgrades();

    @Query("SELECT * FROM click_upgrade WHERE id NOT IN (SELECT idUpgrades FROM user_upgrades WHERE idUser = :idUser)")
    List<ClickUpgrade> getAvailableUpgrades(String idUser);

}