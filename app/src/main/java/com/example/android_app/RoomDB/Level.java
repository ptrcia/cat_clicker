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
    private String id;
    @ColumnInfo(name = "idUpgrade")
    public  String idUpgrade; //foránea
    public int idLevel;
    private int cost;
    private int effect;

    public Level(@NotNull String id, String idUpgrade, int idLevel, int cost, int effect){
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

    public void setIdLevel(int idLevel) {this.idLevel = idLevel;}
    public int getLevel() {return idLevel;}


    public int getCost() {return cost;}
    public void setCost(int cost) {this.cost = cost;}

    public int getEffect() {return effect;}
    public void setEffect(int effect) {this.effect = effect;}
}
