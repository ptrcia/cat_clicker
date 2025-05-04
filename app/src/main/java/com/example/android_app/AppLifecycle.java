package com.example.android_app;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.android_app.RoomDB.GameViewModel;


public class AppLifecycle extends Application implements Application.ActivityLifecycleCallbacks {

    GameViewModel gameViewModel;
    ScoreManager scoreManager;
    GyroscopeManager gyroscopeManager;
    AudioManager audioManager;
    PassiveTimer timer;
    long closedTime;
    long openedTime;
    long elapsedTime;
    boolean isFirstRun;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        gameViewModel = new GameViewModel(this);
        scoreManager = ScoreManager.getInstance();
        audioManager = AudioManager.getInstance(this);
        gyroscopeManager = new GyroscopeManager(getApplicationContext()); // Asegúrate de pasar un contexto válido
        timer = new PassiveTimer(scoreManager);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        Log.d("Clicker->", "onActivityCreated" + activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityStarted"+ activity);

        if (activity instanceof Game) {
            timer.start();
            timer.startAutoSaveTimer(this::saveProgress);
            Log.d("Clicker->", "APPCYCLE -> Timer started for Game");
        }
        preferences = getSharedPreferences("GameData", MODE_PRIVATE);
        isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            editor = preferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.putLong("closedTime", System.currentTimeMillis());
            editor.apply();
            Log.d("Clicker->", "Primera ejecución, no se calcula");
            return;
        }

        //calcular tiempo
        closedTime = preferences.getLong("closedTime", 0);
        openedTime = System.currentTimeMillis();
        elapsedTime = openedTime - closedTime;
        Log.d("Clicker->", "Tiempo transcurrido: " + elapsedTime + " ms");
        if(elapsedTime<=0){
            Log.d("Clicker->", "Tiempo transcurrido negativo, no se calcula");
            return;
        }
       /* if(elapsedTime>1000*60*60*24){
            Log.d("Clicker->", "Tiempo transcurrido mayor a 24 horas, no se calcula");
            return;
        }*/

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("wasDestroyed", false);
        editor.apply();

    }
    public long getElapsedTime(){
        return elapsedTime;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityResumed"+ activity);

        if (activity instanceof Game) {
            timer.start();
            Log.d("Clicker->", "APPCYCLE -> Timer started resumedfor Game");
        }

        if (gyroscopeManager != null) {
            gyroscopeManager.startListening(); // Empieza a escuchar el giroscopio
        }
        audioManager.playMusic();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityPaused"+ activity);

        if (activity instanceof Game) {
            timer.stop();
            //Log.d("Clicker->", "APPCYCLE -> Timer stopped resumedfor Game");
        }

        if (gyroscopeManager != null) {
            gyroscopeManager.stopListening(); // Detén la escucha para ahorrar recursos
        }

        audioManager.pauseMusic();
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

        Log.d("Clicker->", "onActivityStopped"+ activity);

        if (activity instanceof Game) {
            timer.stop();
            //Log.d("Clicker->", "APPCYCLE -> Timer stopped stopppedfor Game");
        }

        saveProgress();
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityDestroyed"+ activity);
        //guardo los datos del usuario
        //los datos de llos niveles a los que tiene cada mejora el ussuario se guardan cada vez que compro una mejora
        //control null
        if(activity instanceof Game){
            saveProgress();
        }
        //audioManager.release();
    }



    public void saveProgress(){
        double pcu = scoreManager.getPassiveValue();
        double acu = scoreManager.getClickValue();
        double score = scoreManager.getScore();

        gameViewModel.updateUserStats(score, pcu, acu);

        //obtener a que hora se cerró
        closedTime = System.currentTimeMillis();

        //Guardar el valor
        preferences = getSharedPreferences("GameData", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putLong("closedTime", closedTime);
        editor.apply();
        Log.d("SAVE PROGRESS" , "Se ha guarado " + closedTime);
    }
}
