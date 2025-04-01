package com.example.android_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.lifecycle.ViewModelProvider;
import com.example.android_app.RoomDB.GameViewModel;
import com.example.android_app.RoomDB.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    static boolean isMuted = false;
    GameViewModel gameViewModel;
    MainActivityViewModel mainActivityViewModel;
    boolean runStarted = false;
    Button butttonContinue;


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
        butttonContinue = findViewById(R.id.buttonContinue);
        ImageButton buttonInfo = findViewById(R.id.info);
        ImageButton buttonVolume = findViewById(R.id.buttonVolume);


        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);




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
                                mainActivityViewModel.resetUserStats();
                                Log.d("Clicker->", "Reseteando UserStats...");
                                Log.d("Clicker->", "Reseteando UserUpgrades...");
                                resetGame();
                                startActivity(new Intent(MainActivity.this, Game.class));

                                SharedPreferences sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("runStarted", true); // Cambia 'true' según tu necesidad
                                editor.apply(); // Guarda los cambios de manera asíncrona
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
               String user = "User1";
                startActivity(new Intent(MainActivity.this, Game.class));
            }
        });

        //Salir
        buttonExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        //Info
        buttonInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir popup
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Realizado por: Patricia S. Gracia Artero");
                alertDialogBuilder
                        .setMessage("Puedes consultar mi porfolio para más proyectos. Gracias por jugar.")
                        .setCancelable(false)
                        .setPositiveButton("Abrir porfolio en el navegador", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //EMPEZAR
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ptrcia.github.io/porfolio/"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
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


        SharedPreferences preferences = getSharedPreferences("GameData", MODE_PRIVATE);
        runStarted = preferences.getBoolean("runStarted", false);
        Log.d("Clicker->", "HOLA A VER SI FUNCIONA isFirstRun -> " + runStarted);

        if (!runStarted) {
            butttonContinue.setEnabled(false);
        }else{
            butttonContinue.setEnabled(true);
        }

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
    public void resetGame() {
        SharedPreferences preferences = getSharedPreferences("GameData", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstRun", true); // Marca como primera ejecución
        editor.putLong("closedTime", 0); // Reinicia closedTime
        editor.apply();
        boolean checkFirstRun =preferences.getBoolean("isFirstRun", true);
        Log.d("Clicker->", "Validación tras reinicio: isFirstRun -> " + checkFirstRun);

        Log.d("Clicker->", "Juego reiniciado, marcando como primera ejecución.");
    }

}