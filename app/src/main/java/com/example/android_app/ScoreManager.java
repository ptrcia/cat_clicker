package com.example.android_app;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScoreManager {

    private int clickValue=1;
    private int passiveValue=0;
    private int score=0;

    //region Instance
    private static ScoreManager instance;

    public static synchronized ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }
    //endregion

    public void ClickActive(){
        score = clickValue + score;
        setScore(score);
        Game.getInstance().UpdateScoreText();
    }
    public void ClickPassive(){
        score = passiveValue + score;
        setScore(score);
        if (Game.getInstance() != null) {
            Game.getInstance().UpdateScoreText();
        }
    }

    public void applyActiveUpgrade(Context context , int cost, int effect){
            score -= cost;
            clickValue += effect;
            setClickValue(clickValue);
            Log.d("Clicker-> ", "Has comprado la mejora");
            Log.d("Clicker-> ", "Score->" + score);
            Log.d("Clicker-> ", "ClickValue->" + clickValue);
             Log.d("Clicker->", "Game instance: " + Game.getInstance());

        if (Game.getInstance() != null) {
            Game.getInstance().UpdateScoreText();
        } else {
            Log.e("Clicker->", "Game instance is null");
        }
         //Audio
            checkAudio(context);
    }
    public void applyPassiveUpgrade(Context context , int cost, int effect){
            score -= cost;
            passiveValue += effect;
        setPassiveValue(passiveValue);
            Log.d("Clicker-> ", "Has comprado la mejora");
            Log.d("Clicker-> ", "Score->" + score);
            Log.d("Clicker-> ", "PassiveValue->" + passiveValue);
            Game.getInstance().UpdateScoreText();
            //Audio
            checkAudio(context);
    }

    public void applyTimeBetweenSesions(long time){

        long secondsPassed = (time/1000);
        // Aqui hay que hacer una progresión segun el tiempo que haya pasado y los puntos que tenga
        if(secondsPassed<=0){
            return;
        }

        if (getPassiveValue() <= 0) {
            Log.e("Clicker->", "Passive value is not initialized. Skipping session calculation.");
            return;
        }
        
        int pointsGained = (int) secondsPassed * getPassiveValue();
        score += pointsGained;
        setScore(score);

        if (Game.getInstance() != null) {
            Game.getInstance().showTimeScore(pointsGained);

            Game.getInstance().UpdateScoreText();
        }

        Log.d("Clicker-> ", "SM Has ganado: "+ (int) secondsPassed * getPassiveValue() +"puntos por estar volver a la aplicación: Han pasado "+ secondsPassed +" segundos, y tu passiveValue es: "+ getPassiveValue());
        Log.d("Clicker-> ", "SM Score->" + score);
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
    public void setScore(int score){
        this.score = score;
    }
    public int getScore(){
        return score;
    }
    public int getClickValue() {
        return clickValue;
    }

    public void setClickValue(int clickValue) {
        this.clickValue = clickValue;
    }

    public void setPassiveValue(int passiveValue) {
        this.passiveValue = passiveValue;
    }
    //endregion
}
