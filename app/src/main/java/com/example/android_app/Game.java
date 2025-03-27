package com.example.android_app;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.Random;

public class Game extends AppCompatActivity {

    private static Game instance;
    static boolean isMuted ;
    private GyroscopeManager gyroscopeManager;
    private ImageView currentImage;
    GameViewModel gameViewModel;
    TextView textScore;
    TextView clickValueText;
    TextView passiveValueText;
    TextView timeScoreText;
    TextView catBonusText;
    TextView catBonusNumber;
    ScoreManager scoreManager;
    FrameLayout mainLayout;
    String user = "User1";
    int catCount;

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
        mainLayout = findViewById(R.id.mainLayout);
        textScore = findViewById(R.id.scoreText);
        clickValueText = findViewById(R.id.clickValueText);
        passiveValueText = findViewById(R.id.passiveValueText);
        timeScoreText = findViewById(R.id.timeScoreText);
        catBonusText = findViewById(R.id.catBonusText);
        catBonusNumber = findViewById(R.id.catBonusNumber);
        scoreManager = ScoreManager.getInstance();

        //region Audio
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
        //endregion

        //reseteo gatitos
        catCount = 0;

        //Giroscopio
        gyroscopeManager = new GyroscopeManager(this);

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
                        buttonClickScore.animate().scaleX(1f).scaleY(1f).setDuration(50).start();
                    }
                }).start();

                //Reference Script Score
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
    //region Imagen

    //imgLayout
    public void addImage(Context context, String id){

        //contar gatitos
        catCount ++;
        if(catCount > 0){
            catBonusText.setText("¡Gatitos activos!");
            String formatedBonus = NumberFormatter.formatNumber(applyCatBonus());
            catBonusNumber.setText("+"+formatedBonus);
        }else{
            catBonusNumber.setText("");
        }

        //Imagen
        ImageView newImage = new ImageView(context);
        newImage.setLayoutParams(new FrameLayout.LayoutParams(
                100, // Ancho
                100  // Alto
        ));
        String resourceName = "upgradecat" + id;
        int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        newImage.setImageResource(resourceId); // Imagen del gatito (reemplaza con tu recurso)
        newImage.setX((float) (Math.random() * (mainLayout.getWidth() - 100))); // Posición aleatoria en eje X
        newImage.setY(-100);
        //añadirslo
        mainLayout.addView(newImage);
        StartFallAnimation(newImage, mainLayout);
        //Log.d("addImage", "Imagen añadida al contenedor. Coordenadas iniciales - X: " + img.getX() + ", Y: " + img.getY());
        //SphereImages(img);
    }
    public int applyCatBonus(){
        if(catCount > 0) {
            int bonus = catCount * scoreManager.getPassiveValue();
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
        gyroscopeManager.startListening(); //?????
    }

    //endregion
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
        alertDialogBuilder.setTitle("¡Hola de nuevo!");
        alertDialogBuilder
                .setMessage("¡En tu ausencia se han acumulado  "+pointsGained+" puntos! (PCU: " + scoreManager.getPassiveValue()+")")
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

                String formattedClickValue = NumberFormatter.formatNumber(scoreManager.getClickValue());
                clickValueText.setText(formattedClickValue + "/click");

                String formattedPassiveValue = NumberFormatter.formatNumber(scoreManager.getPassiveValue());
                passiveValueText.setText(formattedPassiveValue + "/s");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Clicker->", "onResume en game");
    }
}