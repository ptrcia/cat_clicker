package com.example.android_app.RoomDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_upgrades",
        foreignKeys = @ForeignKey(entity = UpgradesUser.class,
                parentColumns = "id",
                childColumns = "idUpgrades",
                onDelete = ForeignKey.CASCADE))
public class UpgradesUser {
@PrimaryKey(autoGenerate = true)
    private int id;
    private int userLevel;
    private int userUpgrade;
    private int idUpgrades;

    public UpgradesUser(int id, int userUpgrade, int userLevel, int idUpgrades) {
        this.id = id;
        this.userUpgrade = userUpgrade;
        this.userLevel = userLevel;
        this.idUpgrades = idUpgrades;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;}

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
