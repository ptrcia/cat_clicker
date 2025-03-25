package com.example.android_app;

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
    ScoreManager scoreManager;
    FrameLayout mainLayout;
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
        mainLayout = findViewById(R.id.mainLayout);
        textScore = findViewById(R.id.scoreText);
        clickValueText = findViewById(R.id.clickValueText);
        passiveValueText = findViewById(R.id.passiveValueText);
        timeScoreText = findViewById(R.id.timeScoreText);
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


        mainLayout.addView(newImage);

        StartFallAnimation(newImage, mainLayout);
        //Log.d("addImage", "Imagen añadida al contenedor. Coordenadas iniciales - X: " + img.getX() + ", Y: " + img.getY());
        //SphereImages(img);
    }

    private void StartFallAnimation(ImageView img, ViewGroup container){
        float targetY = container.getHeight() - img.getHeight()-86;
        ObjectAnimator fall = ObjectAnimator.ofFloat(img, "translationY", targetY);
        fall.setDuration(3000); // Duración de la caída en milisegundos
        fall.setInterpolator(new AccelerateInterpolator());

        fall.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("FallAnimation", "Imagen llegó al fondo. Coordenadas - X: " + img.getX() + ", Y: " + img.getY());
            }
        });
        fall.start();
        gyroscopeManager.startListening();
    }
    /*
    public void GyroPosition( float x, float y){

        if (currentImage == null) {return;  }
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            View child = mainLayout.getChildAt(i);
            if (child instanceof ImageView) {
                float currentX = child.getX();
                float currentY = child.getY();
                Log.d("GyroPosition", "Posición anterior - X: " + currentX + ", Y: " + currentY);

                // Ajustar las posiciones según el giro
                float newX = currentX + (x * 5); // Escala de movimiento horizontal
                float newY = currentY - (y * 5); // Escala de movimiento vertical

                // Evitar que las imágenes salgan fuera del contenedor
                newX = Math.max(0, Math.min(newX, mainLayout.getWidth() - child.getWidth()));
                newY = Math.max(0, Math.min(newY, mainLayout.getHeight() - child.getHeight()));

                child.setX(newX);
                child.setY(newY);
                Log.d("GyroPosition", "Nueva posición establecida - X: " + newX + ", Y: " + newY);

            }
        }
        updatePhysics();
    }

    private void updatePhysics() {
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            ImageView img1 = (ImageView) mainLayout.getChildAt(i);

            // Verificar colisiones
            for (int j = i + 1; j < mainLayout.getChildCount(); j++) {
                ImageView img2 = (ImageView) mainLayout.getChildAt(j);
                if (checkCollision(img1, img2)) {
                    Log.d("Collision", "Colisión detectada entre imágenes.");
                    // Ajustar posiciones o simular rebotes aquí
                    simulateBounce(img1, img2);
                }
            }
        }
    }
    private boolean checkCollision(ImageView img1, ImageView img2){
        float x1 = img1.getX();
        float y1 = img1.getY();
        float x2 = img2.getX();
        float y2 = img2.getY();
        return (x1 < x2 + img2.getWidth() &&
                x1 + img1.getWidth() > x2 &&
                y1 < y2 + img2.getHeight() &&
                y1 + img1.getHeight() > y2);
    }
    private void simulateBounce(ImageView img1, ImageView img2) {
        // Movimiento inverso en el eje X
        img1.setX(img1.getX() - 10);
        img2.setX(img2.getX() + 10);

        // Animación sutil para representar el rebote
        img1.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).withEndAction(() -> {
            img1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100);
        });
    }*/
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

    public void showTimeScore(long timeScore){
        //Animator animator
        timeScoreText.setText("+ "+timeScore);
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
                .setMessage("!En tu ausencia se han acumulado  "+timeScore+" puntos!")
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