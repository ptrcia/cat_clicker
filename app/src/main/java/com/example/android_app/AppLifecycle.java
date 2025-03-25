package com.example.android_app;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
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
        gyroscopeManager = new GyroscopeManager(getApplicationContext()); // Asegúrate de pasar un contexto válido
        timer = new PassiveTimer(scoreManager);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        Log.d("Clicker->", "onActivityCreated");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityStarted");

        if (activity instanceof Game) {
            timer.start();
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


        closedTime = preferences.getLong("closedTime", 0);
        openedTime = System.currentTimeMillis();
        elapsedTime = openedTime - closedTime;
        Log.d("Clicker->", "Tiempo transcurrido: " + elapsedTime + " ms");
        scoreManager.applyTimeBetweenSesions(elapsedTime);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("wasDestroyed", false);
        editor.apply();

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityResumed");

        if (activity instanceof Game) {
            timer.start();
            Log.d("Clicker->", "APPCYCLE -> Timer started resumedfor Game");
        }

        if (gyroscopeManager != null) {
            gyroscopeManager.startListening(); // Empieza a escuchar el giroscopio
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityPaused");

        if (activity instanceof Game) {
            timer.stop();
            Log.d("Clicker->", "APPCYCLE -> Timer stopped resumedfor Game");
        }

        if (gyroscopeManager != null) {
            gyroscopeManager.stopListening(); // Detén la escucha para ahorrar recursos
        }

        Intent playIntent = new Intent(this, AudioManager.class);
        playIntent.setAction("pauseMusic");
        startService(playIntent);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

        Log.d("Clicker->", "onActivityStopped");

        if (activity instanceof Game) {
            timer.stop();
            Log.d("Clicker->", "APPCYCLE -> Timer stopped stopppedfor Game");
        }


        int pcu = scoreManager.getPassiveValue();
        int acu = scoreManager.getClickValue();
        int score = scoreManager.getScore();

        gameViewModel.updateUserStats(score, pcu, acu);

        //obtener a que hora se cerró
        closedTime = System.currentTimeMillis();

        //Guardar el valor
        preferences = getSharedPreferences("GameData", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putLong("closedTime", closedTime);
        editor.apply();
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d("Clicker->", "onActivityDestroyed");
        //guardo los datos del usuario
        //los datos de llos niveles a los que tiene cada mejora el ussuario se guardan cada vez que compro una mejora
        //control null
        if(activity instanceof Game){
            int pcu = scoreManager.getPassiveValue();
            int acu = scoreManager.getClickValue();
            int score = scoreManager.getScore();

            gameViewModel.updateUserStats(score, pcu, acu);

            //obtener a que hora se cerró
            closedTime = System.currentTimeMillis();

            //Guardar el valor
            preferences = getSharedPreferences("GameData", MODE_PRIVATE);
            editor = preferences.edit();
            editor.putLong("closedTime", closedTime);
            editor.apply();
        }

        int pcu = scoreManager.getPassiveValue();
        int acu = scoreManager.getClickValue();
        int score = scoreManager.getScore();

        gameViewModel.updateUserStats(score, pcu, acu);

        //obtener a que hora se cerró
        closedTime = System.currentTimeMillis();

        //Guardar el valor
        preferences = getSharedPreferences("GameData", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putLong("closedTime", closedTime);
        editor.apply();
    }

    private void checkMutedMusic(){
        Intent playIntent = new Intent(this, AudioManager.class);
        if(!AudioManager.isMutedMusic()){
            playIntent.setAction("playMusic");
        }else {
            playIntent.setAction("pauseMusic");
        }
        startService(playIntent);
    }
}
