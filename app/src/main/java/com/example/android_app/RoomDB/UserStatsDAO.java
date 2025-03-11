package com.example.android_app.RoomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserStatsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserStats userStats);

    @Query("SELECT * FROM user_stats")
    List<UserStats> getAllUserStats();

    @Query("SELECT * FROM user_stats WHERE id = :id LIMIT 1")
    UserStats getUserStatsById(String id);

    @Query("UPDATE user_stats SET totalScore = 0, pcuTotal = 0, acuTotal = 1 WHERE id = :id")
    void resetUserStats( String id);

    @Query("UPDATE user_stats SET totalScore  = :score, pcuTotal = :pcu, acuTotal = :acu WHERE id = :id")
    void updateUserStats(int score, int pcu, int acu, String id);
}
