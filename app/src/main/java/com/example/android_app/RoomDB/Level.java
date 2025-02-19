package com.example.android_app.RoomDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "level_table",
        foreignKeys = @ForeignKey(entity = ClickUpgrade.class,
                parentColumns = "id",
                childColumns = "idUpgrade",
                onDelete = CASCADE))
public class Level {
@PrimaryKey(autoGenerate = true)
    private int id;
    public  int idUpgrade;
    public int idLevel; //clave foránea
    private int cost;
    private int effect;

    public Level(int id, int idUpgrade, int idLevel, int cost, int effect){
        this.id = id;
        this.idUpgrade = idUpgrade;
        this.idLevel = idLevel;
        this.cost = cost;
        this.effect = effect;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdUpgrade() {return idUpgrade;}
    public void setIdUpgrade(int idUpgrade) {this.idUpgrade = idUpgrade;}

    public void setIdLevel(int idLevel) {this.idLevel = idLevel;}
    public int getLevel() {return idLevel;}


    public int getCost() {return cost;}
    public void setCost(int cost) {this.cost = cost;}

    public int getEffect() {return effect;}
    public void setEffect(int effect) {this.effect = effect;}



}
