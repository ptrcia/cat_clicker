package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_app.RoomDB.ClickUpgrade;
import com.example.android_app.RoomDB.Level;
import com.example.android_app.RoomDB.UpgradeFragmentViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UpgradeFragment extends Fragment {

    private static final String ARG_UPGRADE_TYPE = "upgrade_type";
    UpgradeFragmentViewModel viewModel;
    Context context;
    LinearLayout container;
    TextView title;
    String userId = "User1";
    String upgradeType;


    //instancia segun el tipo
    public static UpgradeFragment newInstance(String upgradeTypeInput) {
        UpgradeFragment fragment = new UpgradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UPGRADE_TYPE, upgradeTypeInput);
        fragment.setArguments(args);
        //upgradeType = upgradeTypeInput;
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
                requireActivity().getSupportFragmentManager().popBackStack();
                //Intent myIntent = new Intent(context, Game.class);
                //startActivity(myIntent);
            }
        });
        //Boton de atras nativo
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Intent myIntent = new Intent(context, Game.class);
                //startActivity(myIntent);
                if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    requireActivity().getSupportFragmentManager().popBackStack(); // Quita el fragmento de la pila
                } else {
                    requireActivity().finish(); // Si no hay fragmentos en la pila, cierra la actividad
                }
            }
        });

        // Mostrar algo básico si no hay datos
        assert getArguments() != null;
       upgradeType = getArguments().getString(ARG_UPGRADE_TYPE);
        title.setText("Mejora " + upgradeType);

        //FormatUI("Name", "Description", "Id", 0, 0);

        //Obtener todas las mejoras del tipo que sea y meterlas en filterdUpgrades
        //upgradeType te lo da el boton al pulsar y userID lo hardcodeo

        viewModel.pollo.observe(getViewLifecycleOwner(), pollitos-> {
            //CUando este valor cambie, el codigo de aqui se ejecutará
            Log.d("Pollo", "Pollo ha cambiado a :" + pollitos);
                });

        viewModel.filteredUpgrades.observe(getViewLifecycleOwner(), upgrades -> {
            // Crear una copa para que no haya ocnfluico
            Map<ClickUpgrade, Level> upgradesCopy = new HashMap<>(upgrades);

            // Convertir el mapa a una lista de entradas
            List<Map.Entry<ClickUpgrade, Level>> sortedUpgrades = new ArrayList<>(upgradesCopy.entrySet());

            // ORDENAR POR EL id
            Collections.sort(sortedUpgrades, new Comparator<Map.Entry<ClickUpgrade, Level>>() {
                @Override
                public int compare(Map.Entry<ClickUpgrade, Level> e1, Map.Entry<ClickUpgrade, Level> e2) {
                    int lastDigit1 = Integer.parseInt(e1.getKey().getId().replaceAll("\\D+", ""));
                    int lastDigit2 = Integer.parseInt(e2.getKey().getId().replaceAll("\\D+", ""));
                    return Integer.compare(lastDigit2, lastDigit1);
                }
            });
            //para la UI
            for (Map.Entry<ClickUpgrade, Level> e : sortedUpgrades) {
                Log.d("Fragment-> ", "Upgrade: " + e.getKey().getName() + ", ID: " + e.getKey().getId() + ", Description: " + e.getKey().getDescription() + ", Level: " + e.getValue().getIdLevel() + ", Cost: " + e.getValue().getCost() + ", Effect: " + e.getValue().getEffect());
                FormatUI(e.getKey().getName(), e.getKey().getDescription(), e.getKey().getId(), e.getValue().getCost(), e.getValue().getEffect());
            }
        });

       return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getUpgradesTypeUserLevel(upgradeType, userId);
    }

    //Volcado UI
    private void FormatUI(String name, String description, String id, int cost, int effect) {

        //Layout
        LinearLayout newLayout2 = new LinearLayout(context);
        newLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newLayout2.setOrientation(LinearLayout.HORIZONTAL);
        newLayout2.setPadding(0, dpToPx(10), 0, 0);
        newLayout2.setBackgroundColor(Color.parseColor("#F7EDE2"));

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

        //Nivel
        TextView newLevel = new TextView(context);
        LinearLayout.LayoutParams levelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        levelParams.setMarginStart(dpToPx(30));
        newLevel.setLayoutParams(titleParams);
        newLevel.setText(String.valueOf(id));
        newLevel.setTextSize(20);
        Typeface typefaceLevel = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newLevel.setTypeface(typefaceLevel);
        newLevel.setTextColor(Color.BLUE);

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

        //Effect
        TextView newEffect = new TextView(context);
        LinearLayout.LayoutParams costEfect = new LinearLayout.LayoutParams(
                //dpToPx(300),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        costEfect.setMarginStart(dpToPx(30));
        newEffect.setLayoutParams(titleParams);
        newEffect.setText(String.valueOf(effect));
        newEffect.setTextSize(20);
        Typeface typefaceEffect = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newEffect.setTypeface(typefaceEffect);
        newEffect.setTextColor(Color.RED);

        //Button
        Button newButton = new MaterialButton(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(30, 0, 10, 0);
        newButton.setLayoutParams(buttonParams);
        newButton.setText("Mejorar");
        newButton.setAllCaps(false);
        newButton.setBackgroundColor(Color.parseColor("#8f2d56"));
        newButton.setTextSize(20);
        newButton.setTextColor(Color.parseColor("#F7EDE2"));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newButton.setTypeface(typeface);

        //FUNCINALIDAD
        newButton.setTag(R.id.cost_tag, cost);
        newButton.setTag(R.id.effect_tag, effect);
        //EL PULSAR
        newButton.setOnClickListener(ButtonUpgrade);


        //añadir
        //newLayout2.addView(newImg);
        newLayout2.addView(newTitle);
        newLayout2.addView(newCost);
        newLayout2.addView(newEffect);
        newLayout2.addView(newLevel);
        newLayout2.addView(newButton);


        // Añadir el nuevo LinearLayout al contenedor
        container.addView(newLayout2, 0);
        Log.d("Clicker -> ", "FormatUI: " + name + ", Id: " + id + ", Description: " + description + ", Cost: " + cost + ", Effect: " + effect);
    }


    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private final View.OnClickListener ButtonUpgrade = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Obtener la mejora a partir del tag del botón

            int cost = (int) view.getTag(R.id.cost_tag);
            int effect = (int) view.getTag(R.id.effect_tag);

            Log.d("Clicker->", "Coste: " + cost + ", Efecto: " + effect);

            if(upgradeType.equals("Active"))ScoreManager.getInstance().applyActiveUpgrade(cost, effect);
            else if(upgradeType.equals("Passive"))ScoreManager.getInstance().applyPassiveUpgrade(cost, effect);

            //Audio
            if (!AudioManager.isMutedMusic()) {
                Intent playIntent = new Intent(requireActivity(), AudioManager.class);
                playIntent.setAction("playSFX");
                playIntent.putExtra("resourceID", R.raw.purchase);
                requireActivity().startService(playIntent);
            }
        }
    };
}




