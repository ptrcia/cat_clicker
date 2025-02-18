package com.example.android_app.RoomDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "user_upgrades",
        foreignKeys = @ForeignKey(entity = UpgradesUser.class,
                parentColumns = "id",
                childColumns = "idUpgrades",
                onDelete = ForeignKey.CASCADE))
public class UpgradesUser {

    private int userLevel;
    private int userUpgrade;
    private int idUpgrades;

    public UpgradesUser(int userUpgrade, int userLevel, int idUpgrades) {
        this.userUpgrade = userUpgrade;
        this.userLevel = userLevel;
        this.idUpgrades = idUpgrades;

    }

    public int getUserLevel() {
        return userLevel;
    }
    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserUpgrade() {
        return userUpgrade;
    }
    public void setUserUpgrade(int userUpgrade) {
        this.userUpgrade = userUpgrade;
    }


    public int getIdUpgrades() {
        return idUpgrades;
    }
    public void setIdUpgrades(int idUpgrades) {
        this.idUpgrades = idUpgrades;
    }
}
