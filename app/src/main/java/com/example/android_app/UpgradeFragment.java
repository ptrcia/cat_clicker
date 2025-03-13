package com.example.android_app;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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

import android.view.View.OnTouchListener;
import android.widget.Toast;

public class UpgradeFragment extends Fragment {

    private static final String ARG_UPGRADE_TYPE = "upgrade_type";
    UpgradeFragmentViewModel viewModel;
    Context context;
    LinearLayout container;
    TextView title;
    String userId = "User1";
    String upgradeType;

    String userLevel ="0";
    String idUpgrade ="";



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

            }
        });
        //Boton de atras nativo
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

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

        //Metodo para gesto de hacia abajo
        /*container.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //animacion para bajar el tragmento
                    v.animate()
                            .translationY(1000f)
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    //y retroceder

                                    requireActivity().getSupportFragmentManager().popBackStack(); //volver hacia atrás
                                }
                            })
                            .start();
                   // v.performClick(); //que no ionteractue con el cli
                    return true;
                } else {
                    return false;
                }
            }
        });*/

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

            //container.removeAllViews();//limpiar la vista
            //para la UI
            for (Map.Entry<ClickUpgrade, Level> e : sortedUpgrades) {
                Log.d("Fragment-> ", "Upgrade: " + e.getKey().getName() + ", ID: " +  e.getValue().getIdLevel() + ", Description: " + e.getKey().getDescription() + ", Level: " + e.getValue().getIdLevel() + ", Cost: " + e.getValue().getCost() + ", Effect: " + e.getValue().getEffect());
                FormatUI(e.getKey().getName(), e.getKey().getDescription(),e.getKey().getId(),  e.getValue().getIdLevel(), e.getValue().getCost(), e.getValue().getEffect());
            }
        });

       return rootView;
    }
    //region Getter y Setters
    public String getUserLevel(){
        return userLevel;
    }
    public void setUserLevel(String level){
        userLevel = level;
    }
    public String getIdUpgrade(){
        return idUpgrade;
    }
    public void setIdUpgrade(String upgrade){
        this.idUpgrade = idUpgrade;
    }
    //endregion

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getUpgradesTypeUserLevel(upgradeType, userId);
    }

    //Volcado UI
    private void FormatUI(String name, String description, String idUpgrade, String idUserLevel, int cost, int effect) {

        //Layout
        LinearLayout newLayout2 = new LinearLayout(context);
        newLayout2.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newLayout2.setOrientation(LinearLayout.HORIZONTAL);
        newLayout2.setPadding(0, dpToPx(10), 0, dpToPx(0));
        newLayout2.setBackgroundColor(Color.parseColor("#F7EDE2"));

        //Título
        TextView newTitle = new TextView(context);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                //(300),
                //ViewGroup.LayoutParams.WRAP_CONTENT,
                0,
                //LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
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
                //ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                //LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        levelParams.setMarginStart(dpToPx(30));
        newLevel.setLayoutParams(levelParams);
        newLevel.setText(String.valueOf(idUserLevel));
        newLevel.setTextSize(20);
        Typeface typefaceLevel = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newLevel.setTypeface(typefaceLevel);
        newLevel.setTextColor(Color.BLACK);

        //Coste
        TextView newCost = new TextView(context);
        LinearLayout.LayoutParams costParams = new LinearLayout.LayoutParams(
                //ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                //LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        costParams.setMarginStart(dpToPx(30));
        newCost.setLayoutParams(costParams);
        newCost.setText(NumberFormatter.formatNumber(cost));
        //newCost.setText(String.valueOf(cost));
        newCost.setTextSize(20);
        Typeface typefaceCost = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newCost.setTypeface(typefaceCost);
        newCost.setTextColor(Color.parseColor("#8f2d56"));

        //Effect
        TextView newEffect = new TextView(context);
        LinearLayout.LayoutParams effectParams = new LinearLayout.LayoutParams(
                //dpToPx(300),
                //ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                //LinearLayout.LayoutParams.WRAP_CONTENT,

                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        effectParams.setMarginStart(dpToPx(30));
        newEffect.setLayoutParams(effectParams);
        if(Objects.equals(upgradeType, "Active"))newEffect.setText(NumberFormatter.formatNumber(effect) + "/ck");
        else if(Objects.equals(upgradeType, "Passive")) newEffect.setText(NumberFormatter.formatNumber(effect) + "/s");
        //newEffect.setText(String.valueOf(effect));
        newEffect.setTextSize(20);
        Typeface typefaceEffect = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newEffect.setTypeface(typefaceEffect);
        newEffect.setTextColor(Color.parseColor("#8f2d56"));

        //Button
        Button newButton = new MaterialButton(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
               // LinearLayout.LayoutParams.WRAP_CONTENT,
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                2f

        );
        buttonParams.setMargins(0, 0, 10, 0);
        newButton.setLayoutParams(buttonParams);
        newButton.setText("Mejorar");
        newButton.setAllCaps(false);
        newButton.setBackgroundColor(Color.parseColor("#8f2d56"));
        newButton.setTextSize(15);
        newButton.setTextColor(Color.parseColor("#F7EDE2"));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.parkinsans_regular);
        newButton.setTypeface(typeface);

        //FUNCINALIDAD
        newButton.setTag(R.id.cost_tag, cost);
        newButton.setTag(R.id.effect_tag, effect);
        newButton.setTag(R.id.idUpgrade_tag, idUpgrade);
        newButton.setTag(R.id.idUserLevel_tag, idUserLevel);

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
        Log.d("Clicker -> ", "FormatUI: " + name + ", Id: " + idUserLevel + ", Description: " + description + ", Cost: " + cost + ", Effect: " + effect);
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
            String idUpgrade = (String) view.getTag(R.id.idUpgrade_tag);
            String idUserLevel = (String) view.getTag(R.id.idUserLevel_tag);

            Log.d("Clicker->", "Coste: " + cost + ", Efecto: " + effect);

           /* Toast.makeText(
                    context,
                    userHasEnoughScore(cost) ? "Has comprado la mejora" + idUpgrade : "No tienes suficiente score",
                    Toast.LENGTH_SHORT
            ).show();*/

            if(userHasEnoughScore(cost)){
                if(upgradeType.equals("Active"))ScoreManager.getInstance().applyActiveUpgrade(requireContext(),  cost, effect);
                else if(upgradeType.equals("Passive"))ScoreManager.getInstance().applyPassiveUpgrade(requireContext(), cost, effect);

                //Poner esa mejora al nivel siguiente

                //Tengo muchas dudas sobre si implementar esto o hacer las consultas.
                //setUserLevel(idUserLevel);
                //setIdUpgrade(idUpgrade);

                viewModel.updateUserLevel(idUpgrade, idUserLevel, upgradeType, userId);

                synchronized (container) {
                    container.removeAllViews();//eliminar las vistas
                }

                    //container.removeAllViews();
                viewModel.getUpgradesTypeUserLevel(upgradeType, userId);
            }else{
                shakeAnimation(view);
            }

        }
    };
    void shakeAnimation(View button){
        //Animacion de que no
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(70);
        shake.setRepeatCount(7);
        shake.setRepeatMode(Animation.REVERSE);
        button.startAnimation(shake);
    }

    private boolean userHasEnoughScore(int cost){
        int score = ScoreManager.getInstance().getScore();
        return score >= cost;
    }
}




