package com.example.android_app.RoomDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "level_table",
        foreignKeys = @ForeignKey(entity = ClickUpgrade.class,
                parentColumns = "id",
                childColumns = "idUpgrade",
                onDelete = CASCADE))
public class Level {

    public  final int idUpgrade;
    public final int idLevel; //clave foránea
    private final int cost;
    private  final  int effect;

    public Level(int idUpgrade, int idLevel, int cost, int effect){
        this.idUpgrade = idUpgrade;
        this.idLevel = idLevel;
        this.cost = cost;
        this.effect = effect;
    }

    public int getIdUpgrade() {return idUpgrade;}

    public int getLevel() {
        return idLevel;
    }

    public int getCost() {
        return cost;
    }

    public int getEffect() {
        return effect;
    }


}
