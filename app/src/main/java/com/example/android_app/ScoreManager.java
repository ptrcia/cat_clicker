package com.example.android_app;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

public class ScoreManager {

    private int clickValue=1;
    private int passiveValue=0;
    private int score=0;
    private String scoreText;
    private String clickValueText;
    private String passiveValueText;

    //region Instance
    private static ScoreManager instance;

    private ScoreManager() {
        PassiveTimer passiveTimer = new PassiveTimer(this);
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
        score = clickValue + score;
        SetScore(score);
        Game.getInstance().UpdateScoreText();
    }
    public void ClickPassive(){
        score = passiveValue + score;
        SetScore(score);
        Game.getInstance().UpdateScoreText();
    }

    public void applyActiveUpgrade(Context context , int cost, int effect){
            score -= cost;
            clickValue += effect;
            Log.d("Clicker-> ", "Has comprado la mejora");
            Log.d("Clicker-> ", "Score->" + score);
            Log.d("Clicker-> ", "ClickValue->" + clickValue);
            Game.getInstance().UpdateScoreText();
            //Audio
            checkAudio(context);
    }
    public void applyPassiveUpgrade(Context context , int cost, int effect){
            score -= cost;
            passiveValue += effect;

            Log.d("Clicker-> ", "Has comprado la mejora");
            Log.d("Clicker-> ", "Score->" + score);
            Log.d("Clicker-> ", "PassiveValue->" + passiveValue);
            Game.getInstance().UpdateScoreText();
            //Audio
            checkAudio(context);
    }

    void checkAudio(Context context){
        //Hacer sonar
        if (!AudioManager.isMutedMusic()) {
            Intent playIntent = new Intent(context, AudioManager.class);
            playIntent.setAction("playSFX");
            playIntent.putExtra("resourceID", R.raw.purchase);
            context.startService(playIntent);
        }
    }

    //region Getter y Setters

    public int getPassiveValue(){
        return passiveValue;
    }
    public void SetScore(int score){
        this.score = score;
    }
    public int getScore(){
        return score;
    }
    public String getClickValueText(){
        clickValueText = String.valueOf(clickValue);
        return clickValueText;
    }
    public String getPassiveValueText(){
        passiveValueText = String.valueOf(passiveValue);
        return passiveValueText;
    }
    public int getClickValue() {
        return clickValue;
    }

    //endregion
}
