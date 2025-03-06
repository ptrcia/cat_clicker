package com.example.android_app;

import android.os.Debug;
import android.util.Log;

public class ScoreManager {

    private int clickValue=1;
    private int score=0;
    private String scoreText;

    //region Instance
    private static ScoreManager instance;

    private ScoreManager() {}

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }
    //endregion

    public void ClickImage(){
        Log.d("Clicker-> ", "Ha entrado en el método. Score->" + score);
        score = clickValue + score;
        SetScore(score);

    }

    //region Getter y Setters
    public void SetClickValue(int clickValue){
        this.clickValue = clickValue;
    }

    public int getClickValue(){
        return clickValue;
    }

    public void SetScore(int score){
        this.score = score;
    }

    public String getScore(){
        scoreText = String.valueOf(score);
        return scoreText;
    }
    //endregion

    public void applyUpgrade(int cost, int effect){
        clickValue += effect;
        Log.d("Clicker-> ", "Ha entrado en el método. Score->" + clickValue);

    }
}
