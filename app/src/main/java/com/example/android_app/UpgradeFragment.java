package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_app.RoomDB.AppDataBase;
import com.example.android_app.RoomDB.ClickUpgrade;
import com.example.android_app.RoomDB.Level;
import com.example.android_app.RoomDB.LevelDAO;
import com.example.android_app.RoomDB.UpgradeFragmentViewModel;
import com.example.android_app.UpgradeFragmentViewModel;

import java.util.List;

public class UpgradeFragment extends Fragment {

    private static String ARG_UPGRADE_TYPE = "upgrade_type";
    UpgradeFragmentViewModel viewModel;
    Context context;
    LinearLayout container;
    TextView title;

    //instancia segun el tipo
    public static UpgradeFragment newInstance(String upgradeType) {
        UpgradeFragmentViewModel viewModel = new ViewModelProvider(this).get(UpgradeFragmentViewModel.class);
        UpgradeFragment fragment = new UpgradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UPGRADE_TYPE, upgradeType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Clicker->", "UpgradeFragment inflando la vista");

        View rootView = inflater.inflate(R.layout.fragment_upgrades, container, false);
        context = rootView.getContext();
        this.container = rootView.findViewById(R.id.container);
        ImageButton buttonBack = rootView.findViewById(R.id.buttonBack);
        title = rootView.findViewById(R.id.title);

        //boton de regreso
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, Game.class);
                startActivity(myIntent);
            }
        });
        //Boton de atras nativo
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent myIntent = new Intent(context, Game.class);
                startActivity(myIntent);
            }
        });

        // Mostrar algo básico si no hay datos
        assert getArguments() != null;
        String upgradeType = getArguments().getString(ARG_UPGRADE_TYPE);
        if (upgradeType == null || upgradeType.isEmpty()) {
            // Si no hay datos, muestra un mensaje simple
            TextView noDataTextView = new TextView(context);
            noDataTextView.setText("No hay datos para mostrar.");
            container.addView(noDataTextView);
        } else {
            // Si hay datos, maneja como normalmente
            viewModel.setUpgradesType(upgradeType);
            title.setText("Mejora " + upgradeType);
            viewModel.getUpgrades().observe(getViewLifecycleOwner(), upgrades -> {
                container.removeAllViews();
                for (ClickUpgrade upgrade : upgrades) {
                    List<Level> levels = getLevelsForUpgrade(upgrade.getId());
                    FormatUI(upgrade.getName(), upgrade.getDescription(), upgrade.getId(), levels);
                }
            });
        /*
        //RoomDB
        viewModel = new ViewModelProvider(this).get(UpgradeFragmentViewModel.class);

        assert getArguments() != null;
        String upgradeType = getArguments().getString(ARG_UPGRADE_TYPE);

        if (upgradeType != null) {
            viewModel.setUpgradesType(upgradeType);//typo
        }

        //Observar cambios en la lista de mejoras
        viewModel.getUpgrades().observe(getViewLifecycleOwner(), upgrades -> {
            container.removeAllViews();
            for (ClickUpgrade upgrade : upgrades) {
                List<Level> levels = getLevelsForUpgrade(upgrade.getId());
                FormatUI(upgrade.getName(), upgrade.getDescription(),upgrade.getId() , levels);
            }*/
        }

        // inflar
        inflateFragment(this.container);
        return rootView;
    }

    //Sacar los niveles de cada mejora
    private List<Level> getLevelsForUpgrade(int upgradeId) {
        LevelDAO levelDao = AppDataBase.getDatabase(getContext()).levelDAO();
        return levelDao.getLevelsForUpgrade(upgradeId);  // Devuelve los niveles para la mejora dada
    }

    private void inflateFragment(LinearLayout container) {
        if (container == null) {
            Log.d("Clicker-> " ,"Container is null!");
            return;
        }

        //Consulta para saer que mejoras y que nivel de mejoras tiene el usuario
        //quioero que esta funcion me devuelva el calor de upgradeuser y level

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

    //Volcado UI
    private void FormatUI(String name, String description, int id, List<Level> levels) {

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
        for (Level level : levels) {
            String levelInfo = "Nivel: " + level.getLevel() + " Costo: " + level.getCost()
                    + " Efecto: " + level.getEffect();

            //Coste
            TextView newCost = new TextView(context);
            LinearLayout.LayoutParams costParams = new LinearLayout.LayoutParams(
                    //dpToPx(300),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            costParams.setMarginStart(dpToPx(30));
            newCost.setLayoutParams(titleParams);
            newCost.setText(String.valueOf(levelInfo));
            newCost.setTextSize(20);
            Typeface typefaceCost = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
            newCost.setTypeface(typefaceCost);
            newCost.setTextColor(Color.RED);

            newLayout2.addView(newCost);
        }


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

        newLayout2.addView(newButton);

        // Añadir el nuevo LinearLayout al contenedor
        container.addView(newLayout2, 0);
        Log.d("FormatUI", "Upgrade: " + name + ", Key: " + id + ", Description: " + description);
    }

    //Pafuncion para el boton supongo que cambiar el dato
    //gameViewModel.insertClickUpgrade(defaultActiveUpgrade1);

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



