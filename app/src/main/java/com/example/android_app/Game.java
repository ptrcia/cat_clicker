package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_app.RoomDB.BaseCallback;
import com.example.android_app.RoomDB.GameViewModel;
import com.example.android_app.RoomDB.UserStats;

public class Game extends AppCompatActivity {

    private static Game instance;
    static boolean isMuted ;

    GameViewModel gameViewModel;
    TextView textScore;
    TextView clickValueText;
    TextView passiveValueText;
    ScoreManager scoreManager;
    String user = "User1";
    public static Game getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        instance = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button buttonPassives = findViewById(R.id.buttonPassives);
        Button buttonActives = findViewById(R.id.buttonActives);
        ImageButton buttonClickScore = findViewById(R.id.buttonClickeableCat);
        ImageButton buttonVolume = findViewById(R.id.buttonVolume);
        textScore = findViewById(R.id.scoreText);
        clickValueText = findViewById(R.id.clickValueText);
        passiveValueText = findViewById(R.id.passiveValueText);
        scoreManager = ScoreManager.getInstance();

        //AUDIO
        Intent playIntent = new Intent(this, AudioManager.class);
        isMuted = AudioManager.isMutedMusic();
        if(isMuted){
            buttonVolume.setImageResource(R.drawable.mute);
            playIntent.setAction("pauseMusic");
            startService(playIntent);
        }else{
            buttonVolume.setImageResource(R.drawable.volume);
            playIntent.setAction("playMusic");
            startService(playIntent);
        }

        //RoomDB
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        //Coger los datos de la base de datos
        gameViewModel.userStatsLiveData.observe(this, userStats -> {
            if (userStats != null) {
                Log.d("Clicker->", " OBSERVE UserStats: Passive " + userStats.getPcuTotal() + " Active " + userStats.getAcuTotal() + " Score " + userStats.getTotalScore());
                scoreManager.setClickValue(userStats.getAcuTotal());
                scoreManager.setPassiveValue(userStats.getPcuTotal());
                scoreManager.setScore(userStats.getTotalScore());
                UpdateScoreText();
            } else {
                Log.e("Clicker->", "UserStats is null");
            }
        });
        Log.d("Clicker->" ,"Calling getUserStats for userId: " + user);

        gameViewModel.getUserStats(user);
        Log.d("Clicker->", "UserStats: Passive" +scoreManager.getPassiveValue() +" Active "+ scoreManager.getClickValue() + " Score " + scoreManager.getScore());



        //region Botones
        buttonActives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View container = findViewById(R.id.container_layout);
                OpenFragment("Active", container);
            }
        });
        buttonPassives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View container = findViewById(R.id.container_layout);
                OpenFragment("Passive", container);
            }
        });

        /*buttonActives.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    View container = findViewById(R.id.container_layout);
                    OpenFragment("Active", container);
                }
                return false;
            }
        });
        buttonPassives.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    View container = findViewById(R.id.container_layout);

                    OpenFragment("Passive", container);
                }
                return false;
            }
        });*/

        buttonClickScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Audio
                if(!AudioManager.isMutedMusic()){
                    Intent playIntent = new Intent(Game.this, AudioManager.class);
                    playIntent.setAction("playSFX");
                    playIntent.putExtra("resourceID", R.raw.tap);
                    startService(playIntent);
                }

                //Animación
                buttonClickScore.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        buttonClickScore.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    }
                }).start();

                //Reference Script Score
                Log.d("Clicker-> ", "Se ha hecho click");
                scoreManager.ClickActive();
                UpdateScoreText();
            }
        });

        //Volumen
        buttonVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAudio(buttonVolume);
            }
        });
        //endregion
    }


    private void checkAudio(ImageButton buttonVolume){
        Intent audioManager = new Intent(Game.this, AudioManager.class);
        isMuted = AudioManager.isMutedMusic();
        Log.d("Clicker-> ", "isMuted:   " + isMuted);

        if(isMuted){
            Log.d("Clicker-> ", "Queremos audio");
            //Queremos audio
            buttonVolume.setImageResource(R.drawable.volume);
            audioManager.setAction("playMusic");
        }else{
            Log.d("Clicker-> ", "No queremos audio");
            //No queremos audio
            buttonVolume.setImageResource(R.drawable.mute);
            audioManager.setAction("pauseMusic");
        }
        isMuted = !isMuted;
        AudioManager.setMutedMusic(isMuted);
        startService(audioManager);
        Log.d("Clicker-> ", "isMuted despues de pulsar?->:   " + isMuted);

    }

    private void OpenFragment(String upgradeType, View container){
        Log.d("Clicker-> ", "Se ha hecho click en: " + upgradeType);
        //abrir

        Fragment fragment = UpgradeFragment.newInstance(upgradeType);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.d("Clicker->", "Transacción de fragmento en progreso");
        transaction.replace(R.id.container_layout, fragment);
        transaction.addToBackStack(null); // Añadir a la pila de retroceso
        transaction.commit();

        /*container.postDelayed(new Runnable() {
            @Override
            public void run() {
                container.animate()
                        .translationY(0f)
                        .setDuration(500)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                            }
                        })
                        .start();
            }
        }, 100);*/
    }

    public void UpdateScoreText(){
        //ejecutar en el hilo principal
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textScore.setText(String.valueOf(scoreManager.getScore()));
                clickValueText.setText(scoreManager.getClickValueText() + "/click");
                passiveValueText.setText(scoreManager.getPassiveValueText() + "/s");
            }
        });
    }
}