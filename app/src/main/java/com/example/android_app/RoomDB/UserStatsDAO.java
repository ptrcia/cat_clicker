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

    /*@Query("SELECT levelActive FROM user_stats")
    LiveData<List<UpgradesUser>> getActiveLevel();*/ //no funciona

    /*
    public LiveData<List<UpgradesUser>> getActiveLevel() {
    return Transformations.map(db.userStatsDAO().getAllUserStats(), userStatsList -> {
        List<UpgradesUser> activeLevels = new ArrayList<>();
        for (UserStats userStats : userStatsList) {
            activeLevels.addAll(userStats.getLevelActive());
        }
        return activeLevels;
    });
}

    * */


}
