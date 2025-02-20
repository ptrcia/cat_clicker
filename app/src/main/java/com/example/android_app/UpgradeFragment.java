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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import java.util.ArrayList;
import java.util.List;

public class UpgradeFragment extends Fragment {

    private static String ARG_UPGRADE_TYPE = "upgrade_type";
    UpgradeFragmentViewModel viewModel;
    Context context;
    LinearLayout container;
    TextView title;
    String user = "User1";

    //instancia segun el tipo
    public static UpgradeFragment newInstance(String upgradeType) {
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

        //Conecta con el viewmodel
        viewModel = new ViewModelProvider(this).get(UpgradeFragmentViewModel.class);

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
/* Esto es otra opcion
        viewModel.getUpgradesType(upgradeType).observe(getViewLifecycleOwner(), new Observer<List<ClickUpgrade>>() {
            @Override
            public void onChanged(List<ClickUpgrade> filteredUpgrades) {
                container.removeAllViews();
                title.setText("Mejora " + upgradeType);
                Log.d("Clicker-> ", "UpgradeType: " + upgradeType);
                Log.d("Clicker-> ", "filteredUpgrades size: " + filteredUpgrades.size());

                // Comprobar la filtración
                Log.d("Clicker-> ", "FilteredUpgrades: " + filteredUpgrades);
                if (filteredUpgrades == null || filteredUpgrades.isEmpty()) {
                    Log.d("Clicker-> ", "No hay datos para mostrar.");
                } else {
                    // Manejar los datos como normalmente
                }
            }

        });*/


        //Obtener todas las mejoras del tipo que sea y meterlas en filterdUpgrades
        viewModel.getUpgradesType(upgradeType).observe(getViewLifecycleOwner(), filteredUpgrades -> {
            container.removeAllViews();
            title.setText("Mejora " + upgradeType);
            Log.d("Clicker-> ", "UpgradeType: " + upgradeType);
            Log.d("Clicker-> ", "filteredUpgrades size: " + filteredUpgrades.size());
//COMPROBAR LA FILTRACION
            Log.d("Clicker-> ", "FilteredUpgrades1: " + filteredUpgrades);
            if (filteredUpgrades == null || filteredUpgrades.isEmpty()) {
                // Si no hay datos, muestra un mensaje simple
                //title.setText("No hay datos para mostrar.");
                Log.d("Clicker-> ", "No hay datos para mostrar.");
                //return;
            }
            // Si hay datos, maneja como normalmente
            title.setText(upgradeType);



/*
            //obtener todas las mejoras del usuario y meterlas en userupgrades
            viewModel.getAllUserUpgrades(user).observe(getViewLifecycleOwner(), userUpgrades -> {
            Log.d("FiltradoMejoras", "User upgrades: " + userUpgrades.toString());
                List<ClickUpgrade> availableUpgrades = new ArrayList<>();

                assert filteredUpgrades != null;
                for (ClickUpgrade upgrade : filteredUpgrades) {
                    int userLevel = getUserUpgradeLevel(userUpgrades, upgrade.getId()); // Obtener nivel del usuario
                    List<Level> levels = getLevelsForUpgrade(); // Obtener niveles de la mejora

                    // Filtrar mejoras que el usuario aún puede mejorar
                    if (userLevel < levels.size()) {
                        availableUpgrades.add(upgrade);
                    }
                }

                // Mostrar mejoras disponibles en la UI
                for (ClickUpgrade upgrade : availableUpgrades) {
                    View upgradeView = createUpgradeView(upgrade);
                    container.addView(upgradeView);
                }
            });*/
        });
        // inflar
        inflateFragment(this.container);
        return rootView;
    }

/*
    //Sacar los niveles de cada mejora
private List<Level> getLevelsForUpgrade(int upgradeId) {
    List<Level> levels = new ArrayList<>();
    viewModel.getLevelsForUpgrade(upgradeId).observe(getViewLifecycleOwner(), levels::addAll);
    return levels;
}
    */
    private void processLevels(List<Level> levels) {
        // Tu lógica para manejar los niveles y actualizar la UI
    }

    private void inflateFragment(LinearLayout container) {
        if (container == null) {
            Log.d("Clicker-> " ,"Container is null!");

        }else{
            Log.d("Clicker-> " ,"Container is not null!");
            //FormatUI();
        }
    }

    //Volcado UI
    private void FormatUI(String name, String description, String id, List<Level> levels) {

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
            newCost.setText(levelInfo);
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




