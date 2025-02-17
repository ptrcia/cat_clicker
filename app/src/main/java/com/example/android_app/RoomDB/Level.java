package com.example.android_app.RoomDB;

public class Level {
    public int level;
    public int cost;
    public int effect;

    public Level(int level, int cost, int effect){
        this.level = level;
        this.cost = cost;
        this.effect = effect;
    }

    public int getLevel(){return level;}
    public void setLevel(int level){this.level = level;}

    public int getCost(){return cost;}
    public void setCost(int cost){this.cost = cost;}

    public int getEffect(){return effect;}
    public void setEffect(int effect){this.effect = effect;}

}
