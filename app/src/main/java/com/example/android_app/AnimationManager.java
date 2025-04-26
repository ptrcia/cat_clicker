package com.example.android_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class AnimationManager {

    private static AnimationManager instance;
    private Context context;

    public void initialize(Context context) {
        this.context = context;
    }
    public static synchronized AnimationManager getInstance() {
        if (instance == null) {
            instance = new AnimationManager();
        }
        return instance;
    }


    public void moveLayoutButtons(LinearLayout layout, boolean isFragmentOpen){
        if(isFragmentOpen){
            layout.animate().translationY(-1450).setDuration(100);

        }else{
            layout.animate().translationY(0).setDuration(1000);
        }
    }
    public void TitleAnimation(View animatedResource){
        ObjectAnimator animacionX = ObjectAnimator.ofFloat(animatedResource, "scaleX", 0.9f);
        ObjectAnimator animacionY = ObjectAnimator.ofFloat(animatedResource, "scaleY", 1f);

        animacionX.setDuration(1500);
        animacionY.setDuration(1500);
        animacionX.setRepeatCount(ObjectAnimator.INFINITE); // Repetir infinitamente
        animacionY.setRepeatCount(ObjectAnimator.INFINITE);
        animacionX.setRepeatMode(ObjectAnimator.REVERSE); // Animación de ida y vuelta
        animacionY.setRepeatMode(ObjectAnimator.REVERSE);
        animacionX.setInterpolator(new LinearInterpolator());
        animacionX.start();
        animacionY.start();
    }

    public void Scaling(View scalingItem){
        scalingItem.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).withEndAction(() -> {
            scalingItem.animate().scaleX(1f).scaleY(1f).setDuration(50).start();
        }).start();
    }
    public void shakeAnimation(View button){
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(70);
        shake.setRepeatCount(7);
        shake.setRepeatMode(Animation.REVERSE);
        button.startAnimation(shake);
    }
    public void animateText(String text, float startX, float startY, FrameLayout layout) {
        // Crear un nuevo TextView con el texto proporcionado
        TextView textView = new TextView(context);
        textView.setText(text);  // Establecer el texto
        textView.setTextSize(25);  // Ajusta el tamaño de la fuente
        textView.setTextColor(Color.parseColor("#90CAF9"));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.glina_script);
        textView.setTypeface(typeface);


        //FrameLayout layout = findViewById(R.id.mainLayout);
        layout.addView(textView);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        int[] layoutLocation = new int[2];
        layout.getLocationOnScreen(layoutLocation);
        params.leftMargin = (int) startX - layoutLocation[0];
        params.topMargin = (int) startY - layoutLocation[1];

        textView.setLayoutParams(params);

        // Animación de movimiento hacia arriba
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(textView, "translationY", 0, -500);
        moveUp.setDuration(1500);

        // Animación de desvanecimiento (de alpha 1 a alpha 0)
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f);
        fadeOut.setDuration(2000);

        // Crear un conjunto de animaciones
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(moveUp, fadeOut);  // Ambas animaciones ocurren simultáneamente

        // Iniciar la animación
        animatorSet.start();

        // Eliminar el TextView cuando la animación termine
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Eliminar el TextView de la vista después de que la animación termine
                layout.removeView(textView);
            }
        });
    }

}
