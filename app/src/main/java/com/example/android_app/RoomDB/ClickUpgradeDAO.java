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
    LiveData<ClickUpgrade> getActiveUpgrade1();
    //como añado dos condiciones where a una consulta

    @Query("SELECT * FROM click_upgrade WHERE `key` = :key AND level LIKE '%' || :level || '%' LIMIT 1")
    ClickUpgrade getClickUpgradeByKeyAndLevel(String key, int level);

    @Query("SELECT * FROM click_upgrade WHERE `key` = 'acp_2'")
    LiveData<ClickUpgrade> getActiveUpgrade2();
    /*@Query("SELECT * FROM click_upgrade WHERE `key` = 'acp_2' AND levelKey = 1")
    LiveData<ClickUpgrade> getActiveUpgrade2Level1();
*/
    @Query("SELECT * FROM click_upgrade WHERE `key` = 'pcp_1'")
    LiveData<ClickUpgrade> getPassiveUpgrade1();
    @Query("SELECT * FROM click_upgrade WHERE `key` = 'pcp_2'")
    LiveData<ClickUpgrade> getPassiveUpgrade2();
}
