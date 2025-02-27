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


}
