package com.example.android_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.android_app.RoomDB.AppDataBase;
import com.example.android_app.RoomDB.GameViewModel;

import java.util.Random;

public class Game extends AppCompatActivity {

    private static Game instance;
    private AudioManager audioManager;

    private GyroscopeManager gyroscopeManager;
    private ImageView currentImage;
    GameViewModel gameViewModel;
    TextView textScore;
    TextView clickValueText;
    TextView passiveValueText;
    TextView timeScoreText;
    TextView catBonusText;
    TextView catBonusNumber;
    ImageButton buttonLanguageFlech;
    LinearLayout horizontalFlech;
    ScoreManager scoreManager;
    FrameLayout mainLayout;
    LinearLayout linearBottom;

    boolean areAllActivePurchased= false;
    boolean areAllPassivePurchased= false;

    //volume
    boolean mutedMusic;
    boolean mutedSFX;
    int[] icons;
    ImageButton buttonVolume;


    String user = "User1";
    int catCount;
    String formattedClickValue;
    boolean isLanguageOpen = false;
    public boolean isFragmentOpen= false;
    public static Game getInstance() {
        return instance;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        audioManager = AudioManager.getInstance(this);

        //mde99
        AppDataBase.getInstance().loadMode99Preference(this);
        if(!AppDataBase.getInstance().loadMode99Preference(this)) {
            setContentView(R.layout.activity_game);
        } else{
            setContentView(R.layout.activity_game_mode99);
        }
        Log.d("Mode99", "game "+        AppDataBase.getInstance().loadMode99Preference(this));

        //setContentView(R.layout.activity_game);
        instance = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button buttonPassives = findViewById(R.id.buttonPassives);
        Button buttonActives = findViewById(R.id.buttonActives);
        ImageButton buttonClickScore = findViewById(R.id.buttonClickeableCat);
        buttonVolume = findViewById(R.id.buttonVolume);
        View container = findViewById(R.id.container_layout);
        mainLayout = findViewById(R.id.mainLayout);
        textScore = findViewById(R.id.scoreText);
        clickValueText = findViewById(R.id.clickValueText);
        passiveValueText = findViewById(R.id.passiveValueText);
        timeScoreText = findViewById(R.id.timeScoreText);
        catBonusText = findViewById(R.id.catBonusText);
        catBonusNumber = findViewById(R.id.catBonusNumber);
        scoreManager = ScoreManager.getInstance();
        linearBottom = findViewById(R.id.linearBottom);
        buttonLanguageFlech = findViewById(R.id.buttonLanguageFlech);
        horizontalFlech = findViewById(R.id.horizontalFlech);


        //region Audio
        startAudio();
        //endregion



        //Animaciones iniciales
        AnimationManager.getInstance().initialize(this);
        AnimationManager.getInstance().moveLayoutButtons(this, horizontalFlech, isFragmentOpen, container, mainLayout, linearBottom);
        //Idioma
        LanguageTranslator.getInstance().initialize(this);
        LanguageTranslator.getInstance().initializeButtons();
        LanguageTranslator.getInstance().loadLanguagePreference();
        LanguageTranslator.getInstance().Translate(this, LanguageTranslator.getInstance().getCurrentLanguage());
        //reseteo gatitos
        catCount = 0;

        //Giroscopio
        //gyroscopeManager = new GyroscopeManager(this);

        //RoomDB
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        //Coger los datos de la base de datos
        gameViewModel.userStatsLiveData.observe(this, userStats -> {
            if (userStats != null) {
                Log.d("Clicker->", " OBSERVE UserStats: Passive " + userStats.getPcuTotal() + " Active " + userStats.getAcuTotal() + " Score " + userStats.getTotalScore());
                scoreManager.setClickValue(userStats.getAcuTotal());
                scoreManager.setPassiveValue(userStats.getPcuTotal());
                scoreManager.setScore(userStats.getTotalScore());
                Log.d("Clicker->", "1.PassiveValue actualizado: " + scoreManager.getPassiveValue());
                Log.d("Clicker->", "2.Después de setear los valores: Passive " + scoreManager.getPassiveValue() +
                        " Active " + scoreManager.getClickValue() + " Score " + scoreManager.getScore());

                UpdateScoreText();
                scoreManager.applyTimeBetweenSesions( ((AppLifecycle) getApplication()).getElapsedTime(), scoreManager.getScore());

            } else {
                Log.e("Clicker->", "UserStats es null. No se pueden cargar los datos.");
            }
        });
        Log.d("Clicker->" ," GAME -> elapsedTime: " + ((AppLifecycle) getApplication()).getElapsedTime() + " Score: " + scoreManager.getScore() + " PassiveValue: " + scoreManager.getPassiveValue());

        gameViewModel.getUserStats(user);
        Log.d("Clicker->", " NO OBSERVEUserStats: Passive" +scoreManager.getPassiveValue() +" Active "+ scoreManager.getClickValue() + " Score " + scoreManager.getScore());

        //region Botones
        buttonActives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationManager.getInstance().Scaling(buttonActives); //Animación de click (escalado)
                OpenFragment("Active", container);
            }
        });
        buttonPassives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationManager.getInstance().Scaling(buttonPassives); //Animación de click (escalado)
                OpenFragment("Passive", container);
            }
        });

        buttonClickScore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Audio
                    if (!audioManager.isMutedSFX()) {
                        audioManager.playSFX(R.raw.tap);
                    }

                    // Animación de escalado
                    AnimationManager.getInstance().Scaling(buttonClickScore);

                    //Mode99
                    if(AppDataBase.getInstance().loadMode99Preference(Game.this)){
                        buttonClickScore.setImageResource(R.drawable.catopen);
                    }

                    // Coordenadas del dedo
                    float x = event.getRawX();
                    float y = event.getRawY();
                    Log.d("FingerPosition", "X: " + x + ", Y: " + y);

                    // Efecto animado en esa posición
                    AnimationManager.getInstance().animateText(formattedClickValue, x, y, mainLayout);

                    // Lógica de puntuación
                    scoreManager.ClickActive();
                    UpdateScoreText();

                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(AppDataBase.getInstance().loadMode99Preference(Game.this)){
                        buttonClickScore.setImageResource(R.drawable.catclose);
                    }
                }
                return false;
            }
        });


        //Volumen
        buttonVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentActiveState = audioManager.getActiveAudioState();
                Log.d("mode99", " currentActiveState: " + currentActiveState);
                int nextActiveState = (currentActiveState + 1) % icons.length;
                Log.d("mode99", " nextActiveState: " + nextActiveState);

                buttonVolume.setImageResource(icons[nextActiveState]);

                switch (nextActiveState) {
                    case 0: // Estado 0: Suenan música y efectos (índice 0)
                        audioManager.playAll();
                        break;
                    case 1: // Estado 1: Solo música (índice 1)
                        audioManager.onlyMusic();
                        break;
                    case 2: // Estado 2: Solo efectos (índice 2)
                        audioManager.onlySFX();
                        break;
                    case 3: // Estado 3: Mute total (índice 3)
                        audioManager.muteAll();
                        break;
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
                            if(AppDataBase.getInstance().loadMode99Preference(Game.this)){buttonLanguageFlech.setImageResource(R.drawable.forward99);}
                            else{buttonLanguageFlech.setImageResource(R.drawable.back);}
                        }
                    });
                    isLanguageOpen = false;
                }else{
                    horizontalFlech.animate().translationX(-350).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if(AppDataBase.getInstance().loadMode99Preference(Game.this)){buttonLanguageFlech.setImageResource(R.drawable.back99);}
                            else{buttonLanguageFlech.setImageResource(R.drawable.forward);}

                        }
                    });
                    isLanguageOpen = true;
                }
            }
        });

        //endregion
    }

    private void startAudio(){
        Log.d("AudioManager", "el game pasa por aqui al volver a hacerse");
        if(AppDataBase.getInstance().loadMode99Preference(Game.this)){
            icons = new int[]{R.drawable.volume99, R.drawable.musicon99, R.drawable.musicoff99, R.drawable.mute99};
        }else{
            icons = new int[]{R.drawable.volume, R.drawable.musicon, R.drawable.musicoff, R.drawable.mute};
        }

        mutedMusic = audioManager.isMutedMusic();
        mutedSFX = audioManager.isMutedSFX();
        int activeState = audioManager.getActiveAudioState();
        // Actualizar la interfaz de usuario según el estado del audio
        Log.d("mode99", "main - onCreate -> Leído audio_active_state: " + activeState);

        switch (activeState) {
            case 0: audioManager.playAll(); break;
            case 1: audioManager.onlyMusic(); break;
            case 2: audioManager.onlySFX(); break;
            case 3: audioManager.muteAll(); break;
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

    //region Imagen

    //imgLayout
    public void addImage(Context context, String id){

        //contar gatitos
       /* catCount ++;
        if(catCount > 0){
            catBonusText.setText("¡Gatitos activos!");
            String formatedBonus = NumberFormatter.formatNumber(applyCatBonus());
            catBonusNumber.setText("+"+formatedBonus);
        }else{
            catBonusNumber.setText("");
        }*/

        //Imagen
        ImageView newImage = new ImageView(context);
        newImage.setLayoutParams(new FrameLayout.LayoutParams(
                100, // Ancho
                100  // Alto
        ));
        String resourceName = "upgradecat" + id;
        int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        if(!AppDataBase.getInstance().loadMode99Preference(context)){
            newImage.setImageResource(resourceId);
        }else{
            //newImage.setImageResource(R.drawable.fire);
            Glide.with(context).asGif().load(R.drawable.fire).into(newImage);
        }
        newImage.setX((float) (Math.random() * (mainLayout.getWidth() - 100))); // Posición aleatoria en eje X
        newImage.setY(-100);
        //añadirslo
        mainLayout.addView(newImage);
        StartFallAnimation(newImage, mainLayout);
        //Log.d("addImage", "Imagen añadida al contenedor. Coordenadas iniciales - X: " + img.getX() + ", Y: " + img.getY());
        //SphereImages(img);
    }
    public double applyCatBonus(){
        if(catCount > 0) {
            double bonus = catCount + scoreManager.getPassiveValue();
            //Log.d("Clicker-> ", "applyCatBonus: " + bonus + " =  catCount: " + catCount + "  * passiveValue: " + scoreManager.getPassiveValue());
            return bonus;
        }
        return 0;
    }

    private void StartFallAnimation(ImageView img, ViewGroup container){
        float targetY = container.getHeight() - img.getHeight()-86;
        ObjectAnimator fall = ObjectAnimator.ofFloat(img, "translationY", targetY);
        fall.setDuration(new Random().nextInt(1000) + 200); // Duración de la caída en milisegundos
        fall.setInterpolator(new AccelerateInterpolator());

        fall.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Log.d("FallAnimation", "ICoordenadas - X: " + img.getX() + ", Y: " + img.getY());
                // rebotar
                ObjectAnimator bounce = ObjectAnimator.ofFloat(img, "translationY", targetY - 30, targetY); // Rebote hacia arriba y abajo
                bounce.setDuration(new Random().nextInt(500) + 500);
                bounce.setInterpolator(new BounceInterpolator());
                bounce.start();
            }
        });
        fall.start();
        //gyroscopeManager.startListening(); //?????
    }

    //endregion
    /*private void checkAudio(ImageButton buttonVolume){
        Intent audioManager = new Intent(Game.this, AudioManager.class);
        isMuted = AudioManager.isMutedMusic();
        Log.d("Clicker-> ", "isMuted:   " + isMuted);

        if(isMuted){
            Log.d("Clicker-> ", "Queremos audio");
            //Queremos audio
            if(AppDataBase.getInstance().loadMode99Preference(Game.this)){buttonVolume.setImageResource(R.drawable.volume99);
            }else{buttonVolume.setImageResource(R.drawable.volume);}
            audioManager.setAction("playMusic");
        }else{
            Log.d("Clicker-> ", "No queremos audio");
            //No queremos audio
            if(AppDataBase.getInstance().loadMode99Preference(Game.this)){buttonVolume.setImageResource(R.drawable.mute99);
            }else{buttonVolume.setImageResource(R.drawable.mute);}
            audioManager.setAction("pauseMusic");
        }
        isMuted = !isMuted;
        AudioManager.setMutedMusic(isMuted);
        startService(audioManager);
        Log.d("Clicker-> ", "isMuted despues de pulsar?->:   " + isMuted);

    }*/

    private void OpenFragment(String upgradeType, View container){
        Log.d("Clicker-> ", "Se ha hecho click en: " + upgradeType);
        //abrir
        isFragmentOpen = true;
        AnimationManager.getInstance().moveLayoutButtons(this,horizontalFlech, isFragmentOpen, container, mainLayout, linearBottom);
        Fragment fragment = UpgradeFragment.newInstance(upgradeType);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.d("Clicker->", "Transacción de fragmento en progreso");
        transaction.replace(R.id.container_layout, fragment, "UPGRADE_FRAGMENT_TAG");
        transaction.addToBackStack(null); // Añadir a la pila de retroceso
        transaction.commit();

        container.postDelayed(new Runnable() {
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
        }, 100);
    }

    public void showTimeScore(long pointsGained){
        //Animator animator
        timeScoreText.setText("+ "+pointsGained);
        timeScoreText.animate()
                .alpha(0f)
                .setDuration(1000)
                .setStartDelay(4000)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        timeScoreText.setText("");
                        timeScoreText.setAlpha(1f);
                    }
                });
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Game.this);
        alertDialogBuilder.setTitle(LanguageTranslator.getInstance().getScoredDialog()[0]);
        alertDialogBuilder
                .setMessage(LanguageTranslator.getInstance().getScoredDialog()[1]  +pointsGained+ LanguageTranslator.getInstance().getScoredDialog()[2])
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    public void UpdateScoreText(){
        //ejecutar en el hilo principal
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String formattedScore = NumberFormatter.formatNumber(scoreManager.getScore());
                textScore.setText(formattedScore);

                formattedClickValue = NumberFormatter.formatNumber(scoreManager.getClickValue());
                clickValueText.setText(formattedClickValue + "/click");

                String formattedPassiveValue = NumberFormatter.formatNumber(scoreManager.getPassiveValue());
                passiveValueText.setText(formattedPassiveValue + "/s");
            }
        });
    }

    //region End Game
    void EndGame(String upgradeType, Context context, ViewGroup container){
        //Log.d("Fragment End Game ->", "EndGame: " + upgradeType + " ha terminado");


        if(upgradeType.equals("Active")){
            //crear un alert dialog
            if(areAllPassivePurchased) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinalDialog()[0]);
                builder.setMessage(LanguageTranslator.getInstance().getFinalDialog()[6]);
                builder.setPositiveButton(LanguageTranslator.getInstance().getFinalDialog()[4], (dialog, which) -> {
                    gameViewModel.resetUserStats();

                            Intent intent = new Intent(Game.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            AppDataBase.getInstance().Mode99(this);
                    dialog.dismiss();
                })
                .setNegativeButton(LanguageTranslator.getInstance().getFinalDialog()[5], (dialog, which) -> {
                    Intent intent = new Intent(Game.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                });
                builder.show();
                areAllActivePurchased = true;
                //Log.d("Fragment End Game ->", "EndGame: " + upgradeType + " ha terminado y todas las pasivas están compradas");
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinalDialog()[0]);
                builder.setMessage(LanguageTranslator.getInstance().getFinalDialog()[2]);
                builder.setPositiveButton("Ok", (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.show();
                areAllActivePurchased = true;
                //Log.d("Fragment End Game ->", "EndGame: " + upgradeType + " ha terminado y las pasivas NO están compradas");
            }

        }else if(upgradeType.equals("Passive")){
            if(areAllActivePurchased){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinalDialog()[1]);
                builder.setMessage(LanguageTranslator.getInstance().getFinalDialog()[6 ]);
                builder.setPositiveButton(LanguageTranslator.getInstance().getFinalDialog()[4], (dialog, which) -> {
                    gameViewModel.resetUserStats();
                            Intent intent = new Intent(Game.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            AppDataBase.getInstance().Mode99(this);
                    dialog.dismiss();
                })
                        .setNegativeButton(LanguageTranslator.getInstance().getFinalDialog()[5], (dialog, which) -> {
                            Intent intent = new Intent(Game.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        });
                builder.show();
                areAllPassivePurchased = true;
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinalDialog()[1]);
                builder.setMessage(LanguageTranslator.getInstance().getFinalDialog()[3]);
                builder.setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
                areAllPassivePurchased = true;
            }
        }
    }


    void SecretEnding(String upgradeType){

        if(upgradeType.equals("Active")){
            //crear un alert dialog
            if(areAllPassivePurchased) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinal99Dialog()[0]);
                builder.setMessage(LanguageTranslator.getInstance().getFinal99Dialog()[3]);
                builder.setNegativeButton("twitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //EMPEZAR
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/PatricGracia"));
                        startActivity(browserIntent);
                    }
                });
                builder.setNeutralButton("e-mail", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //EMPEZAR
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:patriciagraciaartero@gmail.com"));
                        startActivity(browserIntent);
                    }
                });
                builder.setPositiveButton("instagram", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //EMPEZAR
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ptrcgracia"));
                        startActivity(browserIntent);
                    }
                });

                builder.show();
                areAllActivePurchased = true;
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinal99Dialog()[0]);
                builder.setMessage(LanguageTranslator.getInstance().getFinal99Dialog()[2]);
                builder.setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
                areAllActivePurchased = true;
            }

        }else if(upgradeType.equals("Passive")){
            if(areAllActivePurchased){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinal99Dialog()[1]);
                builder.setMessage(LanguageTranslator.getInstance().getFinal99Dialog()[3]);
                builder.setNegativeButton("twitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //EMPEZAR
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/PatricGracia"));
                        startActivity(browserIntent);
                    }
                });
                builder.setNeutralButton("e-mail", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //EMPEZAR
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:patriciagraciaartero@gmail.com"));
                        startActivity(browserIntent);
                    }
                });
                builder.setPositiveButton("instagram", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //EMPEZAR
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ptrcgracia"));
                        startActivity(browserIntent);
                    }
                });

                builder.show();
                areAllPassivePurchased = true;
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LanguageTranslator.getInstance().getFinal99Dialog()[1]);
                builder.setMessage(LanguageTranslator.getInstance().getFinal99Dialog()[2]);
                builder.setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
                areAllPassivePurchased = true;
            }
        }
    }
    //endregion


    public void setAreAllActivePurchased(boolean areAllActivePurchased) {this.areAllActivePurchased = areAllActivePurchased;}
    public void setAreAllPassivePurchased(boolean areAllPassivePurchased) {this.areAllPassivePurchased = areAllPassivePurchased;}

    @Override
    protected void onResume() {
        super.onResume();
        startAudio();
        updateVolumeIcon();
    }
}