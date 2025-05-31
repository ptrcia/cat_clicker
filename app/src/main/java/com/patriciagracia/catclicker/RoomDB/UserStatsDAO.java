package com.patriciagracia.catclicker.RoomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserStatsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserStats userStats);

    //coger los datos al inicializar
    @Query("SELECT * FROM user_stats")
    List<UserStats> getAllUserStats();

    @Query("SELECT * FROM user_stats WHERE id = :id LIMIT 1")
    UserStats getUserStatsById(String id);

    @Query("UPDATE user_stats SET totalScore = 0, pcuTotal = 0, acuTotal = 1 WHERE id = :id")
    void resetUserStats( String id);

    @Query("UPDATE user_stats SET totalScore  = :score, pcuTotal = :pcu, acuTotal = :acu WHERE id = :id")
    void updateUserStats(double score, double pcu, double acu, String id);



}
