package com.example.android_app;

import android.os.Debug;
import android.util.Log;

public class ScoreManager {

    private int clickValue=1;
    private int passiveValue=0;
    private int score=0;
    private String scoreText;
    private String clickValueText;
    private String passiveValueText;
    private PassiveTimer passiveTimer;

    //region Instance
    private static ScoreManager instance;

    private ScoreManager() {
        passiveTimer = new PassiveTimer(this);
        passiveTimer.start();
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }
    //endregion

    public void ClickActive(){
        //Log.d("Clicker-> ", "Ha entrado en el método. Score->" + score);
        score = clickValue + score;
        SetScore(score);
        Game.getInstance().UpdateScoreText();
    }
    public void ClickPassive(){
        //Log.d("Clicker-> ", "Ha entrado en el método. Score->" + score);
        score = passiveValue + score;
        SetScore(score);
        Game.getInstance().UpdateScoreText();
    }

    public void applyActiveUpgrade(int cost, int effect){
        if(score >= cost){
            score -= cost;
            clickValue += effect;
            Log.d("Clicker-> ", "Has comprado la mejora");
            Log.d("Clicker-> ", "Score->" + score);
            Log.d("Clicker-> ", "ClickValue->" + clickValue);
            Game.getInstance().UpdateScoreText();
        }else{
            Log.d("Clicker-> ", "No tienes suficiente score");
        }
    }
    public void applyPassiveUpgrade(int cost, int effect){
        if(score >= cost){
            score -= cost;
            passiveValue += effect;

            Log.d("Clicker-> ", "Has comprado la mejora");
            Log.d("Clicker-> ", "Score->" + score);
            Log.d("Clicker-> ", "PassiveValue->" + passiveValue);
            Game.getInstance().UpdateScoreText();
        }else{
            Log.d("Clicker-> ", "No tienes suficiente score");
        }
    }

    //region Getter y Setters
    public void setPassiveValue(int passiveValue){
        this.passiveValue = passiveValue;
    }

    public int getPassiveValue(){
        return passiveValue;
    }
    public void SetScore(int score){
        this.score = score;
    }
    public String getScore(){
        scoreText = String.valueOf(score);
        return scoreText;
    }
    public String getClickValueText(){
        clickValueText = String.valueOf(clickValue);
        return clickValueText;
    }
    public String getPassiveValueText(){
        passiveValueText = String.valueOf(passiveValue);
        return passiveValueText;
    }

    //endregion
}
