package com.example.android_app.RoomDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_stats")
public class UserStats {

    @PrimaryKey
    @NotNull
    String name;
    int upgrades;
    int totalScore;  //Total score
    int pcuTotal;   // Total sum of all the passive upgrades
    int acuTotal;  // Total sum of all the active upgrades
    int level;

    public UserStats(@NonNull String name, int upgrades,int totalScore, int pcuTotal, int acuTotal , int level){
        this.name = name;
        this.upgrades = upgrades;
        this.totalScore = totalScore;
        this.pcuTotal = pcuTotal;
        this.acuTotal = acuTotal;
        this.level  = level;
    }
    @NonNull
    public String getName() {return name;}
    public void setName(@NonNull String name) {this.name = name;}

    public int GetUpgrades(){return upgrades;}
    public void setUpgrades(int upgrades) {this.upgrades = upgrades;}

    public int GetTotalScore(){return totalScore;}
    public void setTotalScore(int totalScore) {this.totalScore = totalScore;}

    public int GetPcuTotal(){return pcuTotal;}
    public void setPcuTotal(int pcuTotal) {this.pcuTotal = pcuTotal;}

    public int GetAcuTotal(){return acuTotal;}
    public void setAcuTotal(int acuTotal) {this.acuTotal = acuTotal;}

    public void setLevel(int level) {this.level = level;}
    public int getLevel() {return level;}


}
