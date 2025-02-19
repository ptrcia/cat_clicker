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
    int totalScore;  //Total score
    int pcuTotal;   // suma total de todos los upgrades pasivos
    int acuTotal;  // suma total de todos los upgrades activos
    int id; //id para relacionarlo con upgradesuser


    public UserStats(@NonNull String name ,int totalScore, int pcuTotal, int acuTotal , int id ) {
        this.name = name;
        this.totalScore = totalScore;
        this.pcuTotal = pcuTotal;
        this.acuTotal = acuTotal;
        this.id = id;
    }
    @NonNull
    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getPcuTotal() {
        return pcuTotal;
    }
    public void setPcuTotal(int pcuTotal) {
        this.pcuTotal = pcuTotal;
    }

    public int getAcuTotal() {
        return acuTotal;
    }
    public void setAcuTotal(int acuTotal) {
        this.acuTotal = acuTotal;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


}
