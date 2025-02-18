package com.example.android_app.RoomDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(tableName = "user_stats")
public class UserStats {

    @PrimaryKey
    @NotNull
    String name;
    int upgrades;
    int totalScore;  //Total score
    int pcuTotal;   // suma total de todos los upgrades pasivos
    int acuTotal;  // suma total de todos los upgrades activos
    @TypeConverters(Converters.class)
    List<UpgradesUser> levelActive;
    @TypeConverters(Converters.class)
    List<UpgradesUser> levelPassive;

    public UserStats(@NonNull String name, int upgrades,int totalScore, int pcuTotal, int acuTotal , List<UpgradesUser> levelActive, List<UpgradesUser> levelPassive) {
        this.name = name;
        this.upgrades = upgrades;
        this.totalScore = totalScore;
        this.pcuTotal = pcuTotal;
        this.acuTotal = acuTotal;
        this.levelActive = levelActive;
        this.levelPassive = levelPassive;
    }
    @NonNull
    public String getName() {return name;}
    public void setName(@NonNull String name) {this.name = name;}

    public int getUpgrades(){return upgrades;}
    public void setUpgrades(int upgrades) {this.upgrades = upgrades;}

    public int getTotalScore(){return totalScore;}
    public void setTotalScore(int totalScore) {this.totalScore = totalScore;}

    public int getPcuTotal(){return pcuTotal;}
    public void setPcuTotal(int pcuTotal) {this.pcuTotal = pcuTotal;}

    public int getAcuTotal(){return acuTotal;}
    public void setAcuTotal(int acuTotal) {this.acuTotal = acuTotal;}

    public List<UpgradesUser> getLevelActive() {return levelActive;}
    public void setLevelActive(List<UpgradesUser> levelActive) {this.levelActive = levelActive;}

    public List<UpgradesUser> getLevelPassive() {return levelPassive;}
    public void setLevelPassive(List<UpgradesUser> levelPassive) {this.levelPassive = levelPassive;}




}
