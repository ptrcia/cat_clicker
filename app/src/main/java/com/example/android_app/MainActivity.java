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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_app.RoomDB.AppDataBase;
import com.example.android_app.RoomDB.GameViewModel;
import com.example.android_app.RoomDB.MainActivityViewModel;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    private AudioManager audioManager;
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
    ImageView bottomImage;
    ImageView topImage;
    ImageView topImage1;
    ImageView topImage2;
    ImageButton buttonVolume;


    //Audio
    int[] icons;
    boolean mutedMusic;
    boolean mutedSFX;
    int[] catSounds = {R.raw.cat_purrs_01, R.raw.cat_purrs_02, R.raw.cat_purrs_03, R.raw.cat_purrs_04, R.raw.catpurrs, R.raw.catmeows, R.raw.cat_meows_02, R.raw.cat_meows_03, R.raw.cat_meows_04, R.raw.cat_meows_05, R.raw.cat_meows_06};

    boolean isLanguageOpen = false;
    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        audioManager = AudioManager.getInstance(this);
        //audioManager.initAudio();

        //Poner una vista dependiendo del modo
        AppDataBase.getInstance().loadMode99Preference(this);
        if(!AppDataBase.getInstance().loadMode99Preference(this)){
            setContentView(R.layout.activity_main);
        }else{
            audioManager.mode99(this);
            setContentView(R.layout.activity_main_mode99);
            bottomImage = findViewById(R.id.bottomImage);
            topImage = findViewById(R.id.topImage);
            topImage1 = findViewById(R.id.topImage1);
            topImage2 = findViewById(R.id.topImage2);
            AnimationManager.getInstance().gifMainMode99(bottomImage, topImage, topImage1, topImage2, this);
        }
        Log.d("Mode99", "main  "+        AppDataBase.getInstance().loadMode99Preference(this));



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
        buttonVolume = findViewById(R.id.buttonVolume);
        titleButton = findViewById(R.id.titleButton);
        title1 = findViewById(R.id.titulo);
        title2 = findViewById(R.id.titulo2);
        buttonLanguage = findViewById(R.id.buttonLanguage);
        buttonLanguageFlech = findViewById(R.id.buttonLanguageFlech);
        horizontalFlech = findViewById(R.id.horizontalFlech);

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //Audio
        startAudio();

        //idioma
        LanguageTranslator.getInstance().initializeButtons();
        LanguageTranslator.getInstance().loadLanguagePreference();
        LanguageTranslator.getInstance().Translate(this, LanguageTranslator.getInstance().getCurrentLanguage());

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
                alertDialogBuilder.setTitle(LanguageTranslator.getInstance().getDialogTexts()[0]);
                alertDialogBuilder
                        .setMessage(LanguageTranslator.getInstance().getDialogTexts()[1])
                        .setCancelable(false)
                        .setPositiveButton(LanguageTranslator.getInstance().getDialogTexts()[2], new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //EMPEZAR
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ptrcia.github.io/porfolio/"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton(LanguageTranslator.getInstance().getDialogTexts()[3], new DialogInterface.OnClickListener() {
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

                int currentActiveState = audioManager.getActiveAudioState();
                int nextActiveState = (currentActiveState + 1) % icons.length;

                buttonVolume.setImageResource(icons[nextActiveState]);

                switch (nextActiveState) {
                    case 0: audioManager.playAll(); break;
                    case 1: audioManager.onlyMusic(); break;
                    case 2: audioManager.onlySFX(); break;
                    case 3: audioManager.muteAll(); break;
                }
                // Actualizar el estado activo en AudioManager
                audioManager.setActiveAudioState(nextActiveState);
                audioManager.saveAudioState();
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
                            if(AppDataBase.getInstance().loadMode99Preference(MainActivity.this)){buttonLanguageFlech.setImageResource(R.drawable.back99);}
                            else{buttonLanguageFlech.setImageResource(R.drawable.back);}
                        }
                    });
                    isLanguageOpen = false;
                }else{
                    horizontalFlech.animate().translationX(-550).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if(AppDataBase.getInstance().loadMode99Preference(MainActivity.this)){buttonLanguageFlech.setImageResource(R.drawable.forward99);}
                            else{buttonLanguageFlech.setImageResource(R.drawable.forward);
                            }
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
                LanguageTranslator.getInstance().SelectLanguage(MainActivity.this);
            }
        });

        //title
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!audioManager.isMutedSFX()) {
                    try {
                        audioManager.playSFX(catSounds[new Random().nextInt(catSounds.length)]);
                    } catch (Exception e) {
                        Log.e("AudioManager", "Excepción al hacer clic en titleButton: " + e.getMessage(), e);

                    }
                }
            }
        });

    }

    private void startAudio(){
        if(audioManager.musicAudio==null){
            audioManager.initAudio();
        }

        if(AppDataBase.getInstance().loadMode99Preference(MainActivity.this)){
            icons = new int[]{R.drawable.volume99, R.drawable.musicon99, R.drawable.musicoff99, R.drawable.mute99};
        }else{
            icons = new int[]{R.drawable.volume, R.drawable.musicon, R.drawable.musicoff, R.drawable.mute};
        }

        mutedMusic = audioManager.isMutedMusic();
        mutedSFX = audioManager.isMutedSFX();
        int activeState = audioManager.getActiveAudioState();
        // Actualizar la interfaz de usuario según el estado del audio
        Log.d("AudioManager", "audio_active_state: " + activeState);

        switch (activeState) {
            case 0: audioManager.playAll();break;
            case 1: audioManager.onlyMusic(); break;
            case 2: audioManager.onlySFX(); break;
            case 3: audioManager.muteAll(); break;
        }


        if (!audioManager.isMutedMusic()) {
            audioManager.playAll(); // O solo playMusic()
        }

        updateVolumeIcon();

    }

    private void updateVolumeIcon() {
        int audioState = audioManager.getActiveAudioState();
        buttonVolume = findViewById(R.id.buttonVolume);
        if (buttonVolume != null && icons != null && audioState >= 0 && audioState < icons.length) {
            buttonVolume.setImageResource(icons[audioState]);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("GameData", MODE_PRIVATE);
        runStarted = preferences.getBoolean("runStarted", false);

        startAudio();
        updateVolumeIcon();
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