package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ScoreManager {

    private double clickValue=1;
    private double passiveValue=0;
    private double score=0;

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
    public void ClickPassive(Double  timePassed, Double bonusCat){
        timePassed = (timePassed == null) ? 0 : timePassed;
        bonusCat = (bonusCat == null) ? 0 : bonusCat;

        score += passiveValue + timePassed + bonusCat;

        setScore(score);
        if (Game.getInstance() != null) {
            Game.getInstance().UpdateScoreText();
        }
    }

    public void applyActiveUpgrade(Context context , double cost, double effect){
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
    public void applyPassiveUpgrade(Context context , double cost, double effect){
        score -= cost;
        passiveValue += effect;
        setPassiveValue(passiveValue);
        Log.d("Clicker->", "Has comprado la mejora");
        Log.d("Clicker->", "SM antes Score->" + score);
        Log.d("Clicker->", "PassiveValue->" + passiveValue);
        Game.getInstance().UpdateScoreText();
        //Audio
        checkAudio(context);
    }

    // Aplicar puntos ganados por estar fuera de la aplicación
    public void applyTimeBetweenSesions( long time, double scoreLastTime){
        long secondsPassed = (time/1000);
        // Aqui hay que hacer una progresión segun el tiempo que haya pasado y los puntos que tenga
        if(secondsPassed<=0){
            Log.e("Clicker->", "SM menor que 0");
            return;
        }
        if (passiveValue <= 0) {
            Log.e("Clicker->", "SM Pasivo no inicializado");
            return;
        }
        double pointsGained = secondsPassed * passiveValue;
        Log.d("Clicker-> ", "SM pointsGained->" + pointsGained);
        Log.d("Clicker-> ", "SM Score antes->" + scoreLastTime);
        scoreLastTime += pointsGained;
        Log.d("Clicker-> ", "SM Score después->" + scoreLastTime);

        if (Game.getInstance() != null) {
            Game.getInstance().showTimeScore((long) pointsGained);
            ClickPassive(pointsGained, null);
        }
        Log.d("Clicker-> ", "SM Has ganado: "+ secondsPassed * passiveValue +" puntos por estar volver a la aplicación: Han pasado "+ secondsPassed +" segundos, y tu passiveValue es: "+ passiveValue);
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

    public double getPassiveValue(){
        return passiveValue;
    }
    public void setScore(double score){
        this.score = score;
    }
    public double getScore(){
        return score;
    }
    public double getClickValue() {
        return clickValue;
    }

    public void setClickValue(double clickValue) {
        this.clickValue = clickValue;
    }

    public void setPassiveValue(double passiveValue) {
        this.passiveValue = passiveValue;
    }
    //endregion
}
