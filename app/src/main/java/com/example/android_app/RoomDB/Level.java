package com.example.android_app.RoomDB;


public class Level {

    public final int level;
    private final int cost;
    private  final  int effect;

    public Level(int level, int cost, int effect){
        this.level = level;
        this.cost = cost;
        this.effect = effect;
    }

    public int getLevel() {
        return level;
    }

    public int getCost() {
        return cost;
    }

    public int getEffect() {
        return effect;
    }


}
