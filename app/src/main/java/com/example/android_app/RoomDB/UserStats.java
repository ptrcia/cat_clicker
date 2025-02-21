package com.example.android_app.RoomDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_stats")
public class UserStats {

    @PrimaryKey
            @NotNull
    String id; //id para relacionarlo con upgradesuser
    String name;
    int totalScore;  //Total score
    int pcuTotal;   // suma total de todos los upgrades pasivos
    int acuTotal;  // suma total de todos los upgrades activos



    public UserStats( @NotNull String id, String name ,int totalScore, int pcuTotal, int acuTotal ) {
        this.name = name;
        this.totalScore = totalScore;
        this.pcuTotal = pcuTotal;
        this.acuTotal = acuTotal;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
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

    @NotNull
    public String getId() {
        return id;
    }
    public void setId( @NotNull String id) {
        this.id = id;
    }

    //metodo to string
    @Override
    public String toString() {
        return "UserStats{" + this.name + this.totalScore + this.pcuTotal + this.acuTotal + this.id +'}';
    }
}
