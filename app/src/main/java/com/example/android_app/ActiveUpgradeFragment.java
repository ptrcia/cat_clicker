package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.android_app.RoomDB.AppDataBase;
import com.example.android_app.RoomDB.ClickUpgrade;
import com.example.android_app.RoomDB.ClickUpgradeDAO;
import com.example.android_app.RoomDB.Level;
import com.example.android_app.RoomDB.QueryTest;
import com.example.android_app.RoomDB.UpgradesUser;
import com.example.android_app.RoomDB.UserStats;

import java.util.List;

public class ActiveUpgradeFragment extends Fragment {

    Context context;
    LinearLayout container;
    private int currentLevelIndex = 0;

    public static ActiveUpgradeFragment newInstance(){
        return new ActiveUpgradeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activeupgrades, container, false);
        context = rootView.getContext();
        this.container = rootView.findViewById(R.id.container);
        ImageButton buttonBack = rootView.findViewById(R.id.buttonBack);

        //boton de regreso
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, Game.class);
                startActivity(myIntent);
            }
        });
        // inflar
        inflateFragment(this.container);
        return rootView;
    }
    //Configuración consultas
    AppDataBase db = AppDataBase.getDatabase(context);
    QueryTest queryTest = new QueryTest(db, this);


    private void inflateFragment(LinearLayout container) {
        if (container == null) {
            Log.d("Clicker-> " ,"Container is null!");
            return;
        }
        Log.d("Clicker-> ", "IOUTSIDE SWITCH");

        //Consulta para saer que mejoras y que nivel de mejoras tiene el usuario
        //quioero que esta funcion me devuelva el calor de upgradeuser y level
        queryTest.getUserStats("User1").observe(getViewLifecycleOwner(), new Observer<UserStats>() {
            @Override
            public void onChanged(UserStats userStats) {
                if (userStats != null) {
                    List<UpgradesUser> activeUpgrades = userStats.getLevelActive();
                    for (UpgradesUser upgrade : activeUpgrades) {
                        int upgradeUser = upgrade.getUserUpgrade();
                        int level = upgrade.getUserLevel();
                        Log.d("Clicker->  ", "Upgrade: " + upgradeUser + ", Level: " + level);
                        ProcessUserUpgrades(upgradeUser, level);
                    }
                }
            }
        });
    }

    //Switch para mostrar las mejoras que me interesan
    private void ProcessUserUpgrades(int upgradeUser, int level) {
        Log.d("Clicker-> ", "INSIDE SWITCH: Upgrade: " + upgradeUser + ", Level: " + level);

        switch (upgradeUser){
            case 0:
                //Consulra las mejoras activas de nivel1

                break;
            case 1:
                //Consulra las mejoras activas de nivel1

        }
    }


    //Procesado y recoger los levsels
    private void processClickUpgrade(String name, String key, String description, List<Level> levels){
        if (levels != null) {
            for (Level level : levels) {
                int levelNumber = level.getLevel();
                int cost = level.getCost();
                int effect = level.getEffect();
                Log.d("Clicker-> ", "Level: " + levelNumber + ", Cost: " + cost + ", Effect: " + effect);
                FormatUI(name, key, description, levels, cost, effect);
            }
        } else {
            Log.d("Clicker-> ", "Levels: null");
        }
    }
    ///EFECTTTTTTTT???????????

    //Volcado UI
    private void FormatUI(String name, String key, String description, List<Level> levels, int cost, int effect) {

        //Layout
        LinearLayout newLayout2 = new LinearLayout(context);
        newLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newLayout2.setOrientation(LinearLayout.HORIZONTAL);
        newLayout2.setPadding(0, dpToPx(10), 0, 0);

        //Img Filter
        //ImageView newImg = new ImageView(this);
        //newImg = image.png;

        //Título
        TextView newTitle = new TextView(context);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                //(300),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMarginStart(dpToPx(10));
        titleParams.setMarginEnd(dpToPx(10));
        newTitle.setLayoutParams(titleParams);
        newTitle.setText(name);
        newTitle.setTextSize(20);
        Typeface typefaceTitle = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newTitle.setTypeface(typefaceTitle);
        newTitle.setTextColor(ContextCompat.getColor(context, R.color.black));
/*
        //Descripción
        TextView newDescription = new TextView(context);
        LinearLayout.LayoutParams desParams = new LinearLayout.LayoutParams(
                //dpToPx(300),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        desParams.setMarginStart(dpToPx(30));
        newDescription.setLayoutParams(titleParams);
        newDescription.setText(description);
        newDescription.setTextSize(20);
        Typeface typefaceDes = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newDescription.setTypeface(typefaceDes);
        newDescription.setTextColor(ContextCompat.getColor(context, R.color.black));
*/
        //Coste
        TextView newCost = new TextView(context);
        LinearLayout.LayoutParams costParams = new LinearLayout.LayoutParams(
                //dpToPx(300),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        costParams.setMarginStart(dpToPx(30));
        newCost.setLayoutParams(titleParams);
        newCost.setText(String.valueOf(cost));
        newCost.setTextSize(20);
        Typeface typefaceCost = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newCost.setTypeface(typefaceCost);
        newCost.setTextColor(Color.RED);

        //Efecto
        TextView newEffect = new TextView(context);
        LinearLayout.LayoutParams effectParams = new LinearLayout.LayoutParams(
                //dpToPx(300),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        effectParams.setMarginStart(dpToPx(30));
        newEffect.setLayoutParams(titleParams);
        newEffect.setText(String.valueOf(effect));
        newEffect.setTextSize(20);
        Typeface typefaceEffect = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newEffect.setTypeface(typefaceEffect);
        newEffect.setTextColor(Color.RED);

        //Nivel
        TextView newLevel = new TextView(context);
        LinearLayout.LayoutParams levelParams = new LinearLayout.LayoutParams(
                //dpToPx(300),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        levelParams.setMarginStart(dpToPx(30));
        newLevel.setLayoutParams(titleParams);
        newLevel.setText(String.valueOf(levels.get(currentLevelIndex).getLevel()));
        newLevel.setTextSize(20);
        Typeface typefaceLvel = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newLevel.setTypeface(typefaceLvel);
        newLevel.setTextColor(Color.BLUE);

        //Button
        Button newButton = new Button(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMarginStart(dpToPx(30));
        newButton.setLayoutParams(buttonParams); // Asignar parámetros al botón
        newButton.setText("Mejorar");
        newButton.setAllCaps(false);
        newButton.setBackgroundColor(Color.CYAN);
        newButton.setGravity(Gravity.END);
        newButton.setTextSize(20);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newButton.setTypeface(typeface);
        newButton.setTextColor(ContextCompat.getColor(context, R.color.black));
        //newButton.setId(note.id*10); //                    <---------
        newButton.setTag(name); //COSAS
        newButton.setOnClickListener(upgradeActive);

        //añadir
        //newLayout2.addView(newImg);
        newLayout2.addView(newTitle);
        //newLayout2.addView(newDescription);
        newLayout2.addView(newCost);
        newLayout2.addView(newEffect);
        newLayout2.addView(newLevel);
        newLayout2.addView(newButton);

        // Añadir el nuevo LinearLayout al contenedor
        container.addView(newLayout2, 0);
        Log.d("FormatUI", "Upgrade: " + name + ", Key: " + key + ", Description: " + description + ", Cost: " + cost + ", Effect: " + effect);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private final View.OnClickListener upgradeActive = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Obtener la nota a partir del tag del botón
            view.getTag();  // La instancia de la nota almacenada en el tag
            Log.d("Clicker-> ", "Upgrade: " + view.getTag() );

            //meter info a la tabla usuario
        }
    };

}


