package com.example.android_app.RoomDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "level_table",
        foreignKeys = @ForeignKey(entity = ClickUpgrade.class,
                parentColumns = "id",
                childColumns = "idUpgrade",
                onDelete = CASCADE),
        indices = {@Index(value = "idUpgrade")})

public class Level {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "idUpgrade")
    public  String idUpgrade; //foránea
    public String idLevel;
    private double cost;
    private double effect;

    public Level(@NotNull String id, String idUpgrade, String idLevel, double cost, double effect){
        this.id = id;
        this.idUpgrade = idUpgrade;
        this.idLevel = idLevel;
        this.cost = cost;
        this.effect = effect;
    }

    @NotNull
    public String getId() {
        return id;
    }
    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getIdUpgrade() {return idUpgrade;}
    public void setIdUpgrade(String idUpgrade) {this.idUpgrade = idUpgrade;}

    public void setIdLevel(String idLevel) {this.idLevel = idLevel;}
    public String getIdLevel() {return idLevel;}


    public double getCost() {return cost;}
    public void setCost(double cost) {this.cost = cost;}

    public double getEffect() {return effect;}
    public void setEffect(double effect) {this.effect = effect;}
}
