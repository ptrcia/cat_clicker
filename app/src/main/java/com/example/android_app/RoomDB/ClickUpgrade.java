package com.example.android_app.RoomDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(tableName = "click_upgrade")
@TypeConverters(Converters.class)
public class ClickUpgrade {

    public String name;
    @PrimaryKey
    @NotNull
    public String key;
    public int levelKey;
    //icono
    public String description;

    @TypeConverters(Converters.class)
    public List<Level> level;


    public ClickUpgrade(String name, @NonNull String key, int levelKey, String description, List<Level> level){
        this.name = name;
        this.key = key;
        this.levelKey = levelKey;
        this.description = description;
        this.level  = level;
    }

    public String getName() {return name;}
    public void setName(String name){this.name = name;}

    @NonNull
    public String getKey() {return key;}
    public void setKey(@NonNull String key) {this.key = key;}

    public int getLevelKey() {return levelKey;}
    public void setLevelKey(int levelKey) {this.levelKey = levelKey;}

    public void setDescription(String description){this.description = description;}
    public String getDescription() {return description;}

    public void setLevel(List<Level> level) {this.level = level;}
    public List<Level> getLevel() {return level;}

}


