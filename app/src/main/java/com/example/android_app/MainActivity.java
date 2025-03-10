package com.example.android_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    static boolean isMuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonExit = findViewById(R.id.buttonExit);
        Button butttonContinue = findViewById(R.id.buttonContinue);
        ImageButton buttonConfig = findViewById(R.id.buttonConfig);
        ImageButton buttonVolume = findViewById(R.id.buttonVolume);

        //Exit
        buttonExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        //New game
        buttonStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Crear una nueva partida borrará tus datos guardados.");
                alertDialogBuilder
                        .setMessage("¿quieres continuar igualmente?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //EMPEZAR
                                startActivity(new Intent(MainActivity.this, Game.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });

        //Continue
        butttonContinue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //CARGAR DATOS
                startActivity(new Intent(MainActivity.this, Game.class));
            }
        });

        //Salir
        buttonExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //GUARDAR DATOS
                finish();
                System.exit(0);
            }
        });

        //Config
        buttonConfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir popup
            }
        });

        //Volumen
        buttonVolume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioManager = new Intent(MainActivity.this, AudioManager.class);
                isMuted = AudioManager.isMutedMusic();
                Log.d("Clicker-> ", "isMuted antes de pulsar?->:   " + isMuted);


                if(isMuted){
                    //Queremos audio
                    Log.d("Clicker-> ", "Queremos audio");
                    buttonVolume.setImageResource(R.drawable.volume);
                    audioManager.setAction("playMusic");
                }else{
                    //No queremos audio
                    Log.d("Clicker-> ", "No queremos audio");
                    buttonVolume.setImageResource(R.drawable.mute);
                    audioManager.setAction("pauseMusic");
                }
                isMuted = !isMuted;
                AudioManager.setMutedMusic(isMuted);
                Log.d("Clicker-> ", "isMuted despues de pulsar?->:   " + isMuted);

                startService(audioManager);
            }
        });
    }

    //Cuando queremos vovler a la actividad principal que se mantenga la configuracion del audio
    //Lo hagoa qui y no en el lifecycle porque necesito cambiar la imagen

    @Override
    protected void onResume() {
        super.onResume();

        ImageButton buttonVolume = findViewById(R.id.buttonVolume);

        Intent playIntent = new Intent(this, AudioManager.class);
        isMuted = AudioManager.isMutedMusic();
        Log.d("Clicker-> ", "isMuted antes de pulsar? mAIN->:   " + isMuted);
        if(isMuted){
            buttonVolume.setImageResource(R.drawable.mute);
            playIntent.setAction("pauseMusic");
            startService(playIntent);
        }else{
            buttonVolume.setImageResource(R.drawable.volume);
            playIntent.setAction("playMusic");
            startService(playIntent);
        }
    }
}