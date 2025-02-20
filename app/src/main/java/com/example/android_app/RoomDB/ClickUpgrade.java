package com.example.android_app.RoomDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "click_upgrade")
public class ClickUpgrade {
    @PrimaryKey
    @NotNull
    public String id; //clave de nivel de la mejora relacionada con clave foránea
    public String name;
    public int level;
    //icono
    public String description;
    public String type;

    public ClickUpgrade( @NotNull String id, String name, int level, String description, String type) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.description = description;
        this.type = type;
    }

    public String getName() {return name;}
    public void setName(String name){this.name = name;}

    public int getLevel() {return level;}
    public void setLevel(int level) {this.level = level;}

    @NotNull
    public String getId() {return id;}
    public void setId(@NotNull String id) {this.id = id;}


    public void setDescription(String description){this.description = description;}
    public String getDescription() {return description;}

    public void setType(String type){this.type = type;}
    public String getType() {return type;}
}



