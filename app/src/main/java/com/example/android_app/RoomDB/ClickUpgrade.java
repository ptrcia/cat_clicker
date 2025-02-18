package com.example.android_app.RoomDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "click_upgrade")
public class ClickUpgrade {
    @PrimaryKey
    public int id; //clave de nivel de la mejora relacionada con clave foránea
    public String name;
    //icono
    public String description;
    public String type;

    public ClickUpgrade( int id, String name, String description, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {return name;}
    public void setName(String name){this.name = name;}


    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getLevelKey() {return id;}
    public void setLevelKey(int id) {this.id = id;}

    public void setDescription(String description){this.description = description;}
    public String getDescription() {return description;}

    public void setType(String type){this.type = type;}
    public String getType() {return type;}
}



