package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public boolean flag(boolean isValue){
        return isValue;
    }

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
        //savePassiveValue(context);
        Log.d("Clicker->", "Has comprado la mejora");
        Log.d("Clicker->", "SM antes Score->" + score);
        Log.d("Clicker->", "PassiveValue->" + passiveValue);
        Game.getInstance().UpdateScoreText();
        //saveScore(context); /////////aqui?
        //Audio
        checkAudio(context);
    }

    public void applyTimeBetweenSesions( long time, int scoreLastTime){

        long secondsPassed = (time/1000);
        // Aqui hay que hacer una progresión segun el tiempo que haya pasado y los puntos que tenga
        if(secondsPassed<=0){
            return;
        }

        if (passiveValue <= 0) {
            Log.e("Clicker->", "Pasivo no inicializado");
            return;
        }

        int pointsGained = (int) secondsPassed * passiveValue;
        Log.d("Clicker-> ", "SM pointsGained->" + pointsGained);
        Log.d("Clicker-> ", "SM Score antes->" + scoreLastTime);
        scoreLastTime += pointsGained;
        setScore( scoreLastTime);
        Log.d("Clicker-> ", "SM Score después->" + scoreLastTime);


        if (Game.getInstance() != null) {
            Game.getInstance().showTimeScore(pointsGained);
            Game.getInstance().UpdateScoreText();
        }

        Log.d("Clicker-> ", "SM Has ganado: "+ (int) secondsPassed * passiveValue +" puntos por estar volver a la aplicación: Han pasado "+ secondsPassed +" segundos, y tu passiveValue es: "+ passiveValue);
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
        Log.d("Clicker-> ", " SM getPassiveValue->" + passiveValue);
        return passiveValue;
    }
    public void setScore(int score){
        this.score = score;
        Log.d("Clicker-> ", " SM setScore->" + score);

    }
    public int getScore(){
        Log.d("Clicker-> ", " SM getScore->" + score);
        return score;
    }
    public int getClickValue() {
        Log.d("Clicker->", "SM Get ClickValue: " + clickValue);
        return clickValue;
    }

    public void setClickValue(int clickValue) {
        this.clickValue = clickValue;
        Log.d("Clicker->", "SM Set ClickValue: " + clickValue);

    }

    public void setPassiveValue(int passiveValue) {
        this.passiveValue = passiveValue;
        Log.d("Clicker->", "SM Set PassiveValue: " + passiveValue);

    }
    //endregion
}
