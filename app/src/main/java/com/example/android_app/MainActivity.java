package com.example.android_app;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.android_app.RoomDB.GameViewModel;
import com.example.android_app.RoomDB.MainActivityViewModel;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;

    static boolean isMuted = false;
    GameViewModel gameViewModel;
    MainActivityViewModel mainActivityViewModel;
    boolean runStarted = false;
    Button butttonContinue;
    Button titleButton;
    TextView title1;
    TextView title2;
    ImageButton buttonLanguageFlech;
    ImageButton buttonLanguage;
    LinearLayout horizontalFlech;


    int[] catSounds = {R.raw.cat_purrs_01, R.raw.cat_purrs_02, R.raw.cat_purrs_03, R.raw.cat_purrs_04, R.raw.catpurrs, R.raw.catmeows, R.raw.cat_meows_02, R.raw.cat_meows_03, R.raw.cat_meows_04, R.raw.cat_meows_05, R.raw.cat_meows_06};

    boolean isLanguageOpen = false;
    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        instance = this;

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
        titleButton = findViewById(R.id.titleButton);
        title1 = findViewById(R.id.titulo);
        title2 = findViewById(R.id.titulo2);
        buttonLanguage = findViewById(R.id.buttonLanguage);
        buttonLanguageFlech = findViewById(R.id.buttonLanguageFlech);
        horizontalFlech = findViewById(R.id.horizontalFlech);

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);



        //idioma
        LanguageTranslator.getInstance().loadLanguagePreference();
        LanguageTranslator.getInstance().Translate(LanguageTranslator.getInstance().getCurrentLanguage());

        //animaicon del tituloi
        AnimationManager.getInstance().TitleAnimation(title1);
        AnimationManager.getInstance().TitleAnimation(title2);

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
                buttonInfo.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).withEndAction(() -> {
                    buttonInfo.animate().scaleX(1f).scaleY(1f).setDuration(50).start();
                }).start();                //Abrir popup
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
        //language
        buttonLanguageFlech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mover imagen a la derecha y cambiar icono
                AnimationManager.getInstance().Scaling(buttonLanguageFlech);

                if(isLanguageOpen){

                    horizontalFlech.animate().translationX(0).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            buttonLanguageFlech.setImageResource(R.drawable.back);
                        }
                    });
                    isLanguageOpen = false;
                }else{
                    horizontalFlech.animate().translationX(-550).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            buttonLanguageFlech.setImageResource(R.drawable.forward);
                        }
                    });
                    isLanguageOpen = true;
                }
            }
        });
        buttonLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationManager.getInstance().Scaling(buttonLanguage);
                LanguageTranslator.getInstance().SelectLanguage();
            }
        });

        //title
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AudioManager.isMutedMusic()) {
                    Intent playIntent = new Intent(MainActivity.this, AudioManager.class);
                    playIntent.setAction("playSFX");
                    playIntent.putExtra("resourceID", catSounds[new Random().nextInt(catSounds.length)]);
                    startService(playIntent);
                }
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