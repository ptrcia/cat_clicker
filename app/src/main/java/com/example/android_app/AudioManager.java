package com.example.android_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import java.util.prefs.Preferences;

public class AudioManager extends Service {
    private MediaPlayer musicAudio;
    private SoundPool SFXAudio;
    private static boolean isMutedMusic = false;
    private static boolean isMutedSFX = false;
   public SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        musicAudio = MediaPlayer.create(this, R.raw.music);
        musicAudio.setLooping(true);

        SFXAudio = new SoundPool.Builder().setMaxStreams(1).build();
        sharedPref = getSharedPreferences("audio", Context.MODE_PRIVATE);
    }


    public static boolean isMutedMusic() {
        return isMutedMusic;
    }
    public static void setMutedMusic(boolean mutedMusic) {
        isMutedMusic = mutedMusic;
    }



    private void playMusic() {
        if (!musicAudio.isPlaying()) {
            musicAudio.start();
        }
    }
    private void pauseMusic() {
        if (musicAudio.isPlaying()) {
            musicAudio.pause();
        }
    }

    //metodo que acepte el audio indicado
    private void playSFX(int resourceID) {
        int SFXid = SFXAudio.load(this, resourceID, 1);
        SFXAudio.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                SFXAudio.play(SFXid, 1, 1, 1, 0, 1);
            }
        });
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "playMusic":
                    playMusic();
                    break;
                case "pauseMusic":
                    pauseMusic();
                    break;
                case "playSFX":
                    int resourceID = intent.getIntExtra("resourceID", -1);
                    if (resourceID != -1) {
                        playSFX(resourceID);
                    }
                    break;
            }
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        musicAudio.stop();
        musicAudio.release();
        SFXAudio.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
