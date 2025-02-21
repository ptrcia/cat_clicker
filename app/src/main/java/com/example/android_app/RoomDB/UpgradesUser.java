package com.example.android_app.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_upgrades",
        foreignKeys = {
                @ForeignKey(entity = UserStats.class,
                        parentColumns = "id",
                        childColumns = "idUser",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = ClickUpgrade.class,
                        parentColumns = "id",
                        childColumns = "idUpgrades",
                        onDelete = ForeignKey.CASCADE)
        }, indices = {@Index(value = "idUser"), @Index(value = "idUpgrades")}
)
public class UpgradesUser {
    @PrimaryKey
@NotNull
    private String id;
    private String userLevel;
    @ColumnInfo(name = "idUpgrades")
    private String idUpgrades;
    @ColumnInfo(name = "idUser")
    private String idUser;

    public UpgradesUser(@NotNull String id, String idUpgrades, String userLevel, String idUser) {
        this.id = id;
        this.idUpgrades = idUpgrades;
        this.userLevel = userLevel;
        this.idUser = idUser;

    }
    @NotNull
    public String getId() {
        return id;
    }
    public void setId(@NotNull String id) {
        this.id = id;}

    public String getUserLevel() {
        return userLevel;
    }
    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getIdUpgrades() {
        return idUpgrades;
    }
    public void setIdUpgrades(String idUpgrades) {
        this.idUpgrades = idUpgrades;
    }


    public String getIdUser() {
        return idUser;
    }
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
