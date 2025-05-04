package com.example.android_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.example.android_app.RoomDB.AppDataBase;

public class AudioManager {

    private static AudioManager instance;
    private Context context;
    public MediaPlayer musicAudio;
    private SoundPool SFXAudio;

    private  boolean isMutedMusic = false;
    private  boolean isMutedSFX = false;
    private int audioState = 0;

    public  SharedPreferences sharedPref;
    private int loadedSoundId = 0; // ID del sonido cargado
    private int currentResourceId = 0; // ID del recurso actual


    private AudioManager(Context context) {
        try {
            this.context = context.getApplicationContext(); // Obtener ApplicationContext
            sharedPref = context.getSharedPreferences("audio_settings", Context.MODE_PRIVATE);
            isMutedMusic = sharedPref.getBoolean("muted_music", false);
            isMutedSFX = sharedPref.getBoolean("muted_sfx", false);

            audioState = sharedPref.getInt("audio_active_state", 0);

            if (!isMutedMusic) {
                boolean useMode99 = AppDataBase.getInstance().loadMode99Preference(context);

                if (useMode99) {
                    musicAudio = MediaPlayer.create(context, R.raw.music99);
                    Log.d("AudioManager", "musicAudio (music99): " + musicAudio);
                } else {
                    musicAudio = MediaPlayer.create(context, R.raw.music);
                    Log.d("AudioManager", "musicAudio (music): " + musicAudio);
                }
                if (musicAudio != null) {
                    musicAudio.setLooping(true);
                } else {
                    Log.e("AudioManager", "MediaPlayer.create() returned null!");
                }
            } else {
                Log.d("AudioManager", "isMutedMusic es true, musicAudio no se inicializa");
            }

            SFXAudio = new SoundPool.Builder().setMaxStreams(1).build();
            SFXAudio.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
                if (status == 0) {
                    Log.d("AudioManager", "SFX Sound loaded successfully, sampleId: " + sampleId);
                    loadedSoundId = sampleId;
                    // Si el recurso cargado es el que se quería reproducir, lo reproducimos ahora
                    if (currentResourceId != 0) {
                        playLoadedSFX();
                        currentResourceId = 0; // Reset
                    }
                } else {
                    Log.e("AudioManager", "Failed to load SFX sound, status: " + status);
                }
            });

        } catch (Exception e) {
            Log.e("AudioManager", "Error en el constructor: " + e.getMessage(), e);
        }
    }

    public void initAudio(){
        musicAudio = MediaPlayer.create(context, R.raw.music);
        SFXAudio = new SoundPool.Builder().setMaxStreams(1).build();
    }

    // Get Instance Method (Singleton)
    public static synchronized AudioManager getInstance(Context context) {
        if (instance == null) {
            instance = new AudioManager(context.getApplicationContext()); // Use applicationContext
        }
        return instance;
    }

    public void mode99(Context context) {
        if (musicAudio != null) {
            musicAudio.stop();
            musicAudio.release();
        }
        Log.d("AudioManager", "mode99 -> musicAudio: " + musicAudio);
        musicAudio = MediaPlayer.create(context, R.raw.music99);
        musicAudio.setLooping(true);
    }

    public void saveAudioState() {
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("muted_music", isMutedMusic);
            editor.putBoolean("muted_sfx", isMutedSFX);
            editor.putInt("audio_active_state", audioState); // Guardar el estado activo actual")
            editor.apply();
            Log.d("AudioManager", "saveAudioState -> Guardado: mutedMusic=" + isMutedMusic + ", mutedSFX=" + isMutedSFX);
        }
    }

    public boolean getMutedMusic() {return sharedPref.getBoolean("muted_music", false);}
    public  boolean getSFXState() {return sharedPref.getBoolean("muted_sfx", false);}
    public  boolean isMutedMusic() {return isMutedMusic;}
    //public  void setMutedMusic(boolean mutedMusic) {isMutedMusic = mutedMusic;}
    public  boolean isMutedSFX() {return isMutedSFX;}
   // public  void setMutedSFX(boolean mutedSFX) {isMutedSFX = mutedSFX;}
    public int getActiveAudioState() {return audioState;}
    public void setActiveAudioState(int state) {audioState = state;}


    public void playMusic() {
        isMutedMusic = false;
        if(musicAudio == null) {
            boolean useMode99 = AppDataBase.getInstance().loadMode99Preference(context);
            if (useMode99) {
                musicAudio = MediaPlayer.create(context, R.raw.music99);
            } else {
                musicAudio = MediaPlayer.create(context, R.raw.music);
            }
        }else{
            musicAudio.start();
        }
    }

    public void pauseMusic() {
        isMutedMusic = true;
        if (musicAudio != null &&musicAudio.isPlaying()) {
            musicAudio.pause();
        }
    }
    public void playSFX(int resourceID) {
        Log.d("AudioManager", "playSFX - resourceID: " + resourceID);
        Log.d("AudioManager", "playSFX - isMutedSFX: " + isMutedSFX);
        if (!isMutedSFX) {
            if (loadedSoundId != 0 && currentResourceId == resourceID) {
                playLoadedSFX(); // Reproducir si ya está cargado y es el recurso correcto
            } else {
                currentResourceId = resourceID;
                loadedSoundId = 0; // Reset
                SFXAudio.load(context, resourceID, 1); // Cargar el sonido
            }
        }
    }

    private void playLoadedSFX() {
        if (loadedSoundId != 0 && SFXAudio != null) {
            SFXAudio.play(loadedSoundId, 1, 1, 0, 0, 1);
        }
    }
    //metodo que acepte el audio indicado
    /*public void playSFX(int resourceID) {
        Log.d("AudioManager", "playSFX - resourceID: " + resourceID);
        Log.d("AudioManager", "playSFX - isMutedSFX: " + isMutedSFX);
        if (!isMutedSFX) {
            SFXAudio.play(SFXAudio.load(context, resourceID, 1), 1, 1, 0, 0, 1);
        }
    }*/

    public void pauseSFX() {
        isMutedSFX = true;
        saveAudioState();
    }

    public void playAll() {
        isMutedMusic = false;
        isMutedSFX = false;
        playMusic();
        saveAudioState();

    }

    public void onlyMusic() {
        playMusic();
        isMutedSFX = true;
        saveAudioState();
    }

    public void onlySFX() {
        pauseMusic();
        isMutedSFX = false;
        saveAudioState();
    }

    public void muteAll() {
        pauseMusic();
        isMutedSFX = true;
        saveAudioState();
    }

    public void release() {
        if (musicAudio != null) {
            musicAudio.stop();
            musicAudio.release();
            musicAudio = null;
        }
        if (SFXAudio != null) {
            SFXAudio.release();
            SFXAudio = null;
        }
    }


}
