package com.example.android_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.android_app.RoomDB.AppDataBase;

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

    public void moveLayoutButtons(Context context, LinearLayout layout, boolean isFragmentOpen, View containerLayout, FrameLayout mainLayout, LinearLayout linearBottom) {

        // Referencia al ConstraintLayout (padre)
        ConstraintLayout constraintLayout = (ConstraintLayout) mainLayout.getParent();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (isFragmentOpen) {

            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //mover flecha
                // layout.animate().translationX(- 530).setDuration(100);
                //Log.d("AnimationManager", "moveLayoutButtons (landscape): " + (containerLayout.getWidth() - 100));
                //mover pantalla
                constraintSet.connect(mainLayout.getId(), ConstraintSet.END, containerLayout.getId(), ConstraintSet.START);
                constraintSet.applyTo(constraintLayout);

            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //mover flecha
                layout.animate().translationY(containerLayout.getHeight() - 3100).setDuration(100);
                //Log.d("AnimationManager", "moveLayoutButtons (portrait): " + (containerLayout.getHeight() - 3100));
                //no mover pantalla
            }
        } else {
            // Restaurar constraint de mainLayout a linearBottom (o a su estado inicial)
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                constraintSet.clear(mainLayout.getId(), ConstraintSet.END);
                constraintSet.connect(mainLayout.getId(), ConstraintSet.END, linearBottom.getId(), ConstraintSet.START);
                constraintSet.applyTo(constraintLayout);
            }
            // Restaurar animaciones a la posición original (se aplica en ambas orientaciones al cerrar)
            layout.animate().translationY(0).setDuration(1000);
            layout.animate().translationX(0).setDuration(1000);
        }
    }


    public void TitleAnimation(View animatedResource) {
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

    // gif animados
    void gifMainmode66(ImageView bottom, ImageView top, ImageView top1, ImageView top2, Context context) {
        Glide.with(context).asGif().load(R.drawable.campfire).into(bottom);
        Glide.with(context).asGif().load(R.drawable.fire).into(top);
        Glide.with(context).asGif().load(R.drawable.fire).into(top1);
        Glide.with(context).asGif().load(R.drawable.fire).into(top2);

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
        Typeface typeface;
        if(AppDataBase.getInstance().loadmode66Preference(context)){
            textView.setTextColor(Color.parseColor("#ffffff"));
            typeface = ResourcesCompat.getFont(context, R.font.hexagothic_display);

        }else {
            textView.setTextColor(Color.parseColor("#90CAF9"));
            typeface = ResourcesCompat.getFont(context, R.font.glina_script);
        }
        textView.setTypeface(typeface);

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
