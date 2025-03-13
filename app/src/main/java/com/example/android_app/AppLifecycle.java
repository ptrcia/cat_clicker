package com.example.android_app;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.android_app.RoomDB.GameViewModel;

public class AppLifecycle extends Application implements Application.ActivityLifecycleCallbacks {

    GameViewModel gameViewModel;
    ScoreManager scoreManager;


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        gameViewModel = new GameViewModel(this);
        scoreManager = ScoreManager.getInstance();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        //checkMutedMusic();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Intent playIntent = new Intent(this, AudioManager.class);
        playIntent.setAction("pauseMusic");
        startService(playIntent);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        int pcu = scoreManager.getPassiveValue();
        int acu = scoreManager.getClickValue();
        int score = scoreManager.getScore();

        gameViewModel.updateUserStats(score, pcu, acu);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

        //guardo los datos del usuario
        //los datos de llos niveles a los que tiene cada mejora el ussuario se guardan cada vez que compro una mejora
        //control null

        int pcu = scoreManager.getPassiveValue();
        int acu = scoreManager.getClickValue();
        int score = scoreManager.getScore();

        gameViewModel.updateUserStats(score, pcu, acu);
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
