package com.example.android_app.RoomDB;

import androidx.lifecycle.LiveData;
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
    LiveData<List<UserStats>> getAllUserStats();

    @Query("SELECT * FROM user_stats WHERE name = :user LIMIT 1")
    LiveData<UserStats> getUserStatsByName(String user);

    @Query("SELECT * FROM user_stats WHERE id = :id LIMIT 1")
    LiveData<UserStats> getUserStats(int id);
}
