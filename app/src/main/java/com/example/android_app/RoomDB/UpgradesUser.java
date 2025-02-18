package com.example.android_app.RoomDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "upgrades_user",
        foreignKeys = @ForeignKey(entity = UserStats.class,
                parentColumns = "name",
                childColumns = "user_stats_name",
                onDelete = ForeignKey.CASCADE))
public class UpgradesUser {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int level;
    public int upgrade;

    public String user_stats_name;


    public UpgradesUser(String user_stats_name,int upgrade, int level ){
        this.user_stats_name = user_stats_name;
        this.upgrade = upgrade;
        this.level = level;

    }

    public int getUserLevel(){return level;}
    public void setUserLevel(int level){this.level = level;}

    public int getUserUpgrade(){return upgrade;}
    public void setUserUpgrade(int upgrade){this.upgrade = upgrade;}

    public String getUserStatsName() { return user_stats_name; }
    public void setUserStatsName(String user_stats_name) { this.user_stats_name = user_stats_name; }

}
