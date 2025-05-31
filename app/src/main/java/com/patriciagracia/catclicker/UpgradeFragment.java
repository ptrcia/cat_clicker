package com.patriciagracia.catclicker;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.patriciagracia.catclicker.RoomDB.AppDataBase;
import com.patriciagracia.catclicker.RoomDB.ClickUpgrade;
import com.patriciagracia.catclicker.RoomDB.Level;
import com.patriciagracia.catclicker.RoomDB.UpgradeFragmentViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class UpgradeFragment extends Fragment {

    View rootView;

    private static final String ARG_UPGRADE_TYPE = "upgrade_type";
    UpgradeFragmentViewModel viewModel;
    ProgressBar progressBar;
    Context context;
    LinearLayout container;
    TextView title;
    String userId = "User1";
    String upgradeType;
    String userLevel ="0";
    String idUpgrade ="";
    ImageView image;
    TextView cost;
    TextView effect;
    TextView level;
    public int buttonId;
    TextView upgradeSecretCost;
    TextView upgradeSecretEffect;

    //instancia segun el tipo
    public static UpgradeFragment newInstance(String upgradeTypeInput) {
        UpgradeFragment fragment = new UpgradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UPGRADE_TYPE, upgradeTypeInput);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        context = getContext();
        if (!AppDataBase.getInstance().loadmode66Preference(context)) {
            rootView = View.inflate(getContext(), R.layout.fragment_upgrades, null);
        } else {
            rootView = View.inflate(getContext(), R.layout.fragment_upgrades_mode66, null);
        }

        context = rootView.getContext();
        this.container = rootView.findViewById(R.id.container);
        ImageButton buttonBack = rootView.findViewById(R.id.buttonBack);
        title = rootView.findViewById(R.id.title);
        progressBar = rootView.findViewById(R.id.progressBar);
        cost = rootView.findViewById(R.id.cost);
        effect = rootView.findViewById(R.id.effect);
        level = rootView.findViewById(R.id.level);


        //Idoma
        LanguageTranslator.getInstance().initializeButtons();
        LanguageTranslator translator = LanguageTranslator.getInstance();
        translator.Translate(context, translator.getCurrentLanguage());


        upgradeSecretCost = rootView.findViewById(R.id.upgrade_cost);
        upgradeSecretEffect = rootView.findViewById(R.id.upgrade_effect);



        //Conecta con el viewmodel
        viewModel = new ViewModelProvider(this).get(UpgradeFragmentViewModel.class);

        //boton de regreso
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonBack.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).withEndAction(() -> {
                    buttonBack.animate().scaleX(1f).scaleY(1f).setDuration(50).start();
                }).start();
                requireActivity().getSupportFragmentManager().popBackStack();
                Game.getInstance().isFragmentOpen = false;
                AnimationManager.getInstance().moveLayoutButtons(context, Game.getInstance().horizontalFlech, Game.getInstance().isFragmentOpen, container, Game.getInstance().mainLayout, Game.getInstance().linearBottom);
            }
        });
        //Boton de atras nativo
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Game.getInstance().isFragmentOpen = false;
                AnimationManager.getInstance().moveLayoutButtons(context, Game.getInstance().horizontalFlech, Game.getInstance().isFragmentOpen, container, Game.getInstance().mainLayout, Game.getInstance().linearBottom);

                if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    requireActivity().finish();
                }
            }
        });

        assert getArguments() != null;
       upgradeType = getArguments().getString(ARG_UPGRADE_TYPE);
       LanguageTranslator.getInstance().renameFragment(upgradeType, title, cost, effect, level);

       //idioma misteriosa mejora missteriosa
        if(upgradeSecretCost != null && upgradeSecretEffect != null){
            if(upgradeType.equals("Active")){
                upgradeSecretCost.setText(LanguageTranslator.getInstance().getSecretUpgradeText()[0]);
                upgradeSecretEffect.setText(LanguageTranslator.getInstance().getSecretUpgradeText()[2]);
            } else if (upgradeType.equals("Passive")) {
                upgradeSecretCost.setText(LanguageTranslator.getInstance().getSecretUpgradeText()[1]);
                upgradeSecretEffect.setText(LanguageTranslator.getInstance().getSecretUpgradeText()[2]);
            }
        }

        //FormatUI("Name", "Description", "Id", 0, 0);

        //Obtener todas las mejoras del tipo que sea y meterlas en filterdUpgrades
        //upgradeType te lo da el boton al pulsar y userID lo hardcodeo

        viewModel.pollo.observe(getViewLifecycleOwner(), pollitos-> {
            //CUando este valor cambie, el codigo de aqui se ejecutará
            Log.d("Pollo", "Pollo ha cambiado a :" + pollitos);
                });

        progressBar.setVisibility(View.VISIBLE);

        //Recoger datos de la database
        viewModel.filteredUpgrades.observe(getViewLifecycleOwner(), upgrades -> {

            if (upgrades == null || upgrades.isEmpty()) {
                Log.d("Fragment End Game ->", "No hay más elementos en la lista." + upgradeType);
                progressBar.setVisibility(View.GONE);
                if(!AppDataBase.getInstance().loadmode66Preference(context)){
                    Game.getInstance().EndGame(upgradeType, context, container);

                }else{
                    Game.getInstance().SecretEnding(upgradeType);
                }
                return;
            }

            // Crear una copia y hacerlo dentro de un syncronized  para que no haya conflicto
            Map<ClickUpgrade, Level> upgradesCopy;
            synchronized (upgrades) {
                upgradesCopy = new HashMap<>(upgrades);
            }

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
                Log.d("Fragment-> ", "Upgrade: " + e.getKey().getName() + ", ID: " +  e.getValue().getIdLevel() + ", Description: " + e.getKey().getDescription() + ", Level: " + e.getValue().getIdLevel() + ", Cost: " + e.getValue().getCost() + ", Effect: " + e.getValue().getEffect());
                FormatUI(e.getKey().getName(), e.getKey().getDescription(),e.getKey().getId(),  e.getValue().getIdLevel(), e.getValue().getCost(), e.getValue().getEffect());
            }
            //upgradesCopy.clear(); //??
            progressBar.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        });
       return rootView;
    }
    //region Getter y Setters
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
    int textSize=17;
    private void FormatUI(String name, String description, String idUpgrade, String idUserLevel, double cost, double effect) {
        String colorbackgroud;
        String titleUpgrade;
        String costeffectbutton;
        Typeface typeface;


        if(AppDataBase.getInstance().loadmode66Preference(getContext()))
        {
            typeface = ResourcesCompat.getFont(context, R.font.hexagothic_display);
            colorbackgroud = "#1D1616";
            costeffectbutton = "#EA906C";
            titleUpgrade = "#FFFFFF";

        }else{

            colorbackgroud = "#FFF8F0";
            costeffectbutton = "#8f2d56";
            titleUpgrade = "#000000";
            typeface = ResourcesCompat.getFont(context, R.font.cleanow);

        }

    //Main Layout
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setPadding(0, dpToPx(10), 0, dpToPx(0));

        //Layout vertical para la imagen
        LinearLayout verticalLayoutImg = new LinearLayout(context);
        verticalLayoutImg.setLayoutParams(new LinearLayout.LayoutParams(
                0, // Ancho proporcional
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));
        verticalLayoutImg.setOrientation(LinearLayout.VERTICAL);
        verticalLayoutImg.setPadding(0, dpToPx(10), 0, dpToPx(0));
        verticalLayoutImg.setBackgroundColor(Color.parseColor(colorbackgroud));

        //Layout vertical para el texto y el botón
        LinearLayout verticalLayoutTextButton = new LinearLayout(context);
        verticalLayoutTextButton.setLayoutParams(new LinearLayout.LayoutParams(
                0, // Ancho proporcional
                LinearLayout.LayoutParams.WRAP_CONTENT,
                4f
        ));
        verticalLayoutTextButton.setOrientation(LinearLayout.VERTICAL);
        verticalLayoutTextButton.setPadding(0, dpToPx(10), 0, dpToPx(0));
        verticalLayoutTextButton.setBackgroundColor(Color.parseColor(colorbackgroud));


        //Layout horizontal para el texto
        LinearLayout horizontalLayoutText = new LinearLayout(context);
        horizontalLayoutText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        horizontalLayoutText.setPadding(0, dpToPx(10), 0, dpToPx(0));
        horizontalLayoutText.setOrientation(LinearLayout.HORIZONTAL);

        //Layout horizontal para el botón
        LinearLayout horizontalLayoutButton = new LinearLayout(context);
        horizontalLayoutButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        horizontalLayoutButton.setPadding(0, dpToPx(10), 0, dpToPx(0));
        horizontalLayoutButton.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayoutButton.setGravity(Gravity.END);

//Espacio para separar
        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));

        //Imagen
        ImageView newImg = new ImageView(context);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(

                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newImg.setLayoutParams(imgParams);
        newImg.setScaleX(0.7f);
        newImg.setScaleY(0.7f);

        //Sacamos el valor numérico del id
        String numberId = idUpgrade.replaceAll("\\D", ""); // Elimina todos los caracteres que no sean dígitos
        if(AppDataBase.getInstance().loadmode66Preference(getContext())){
            newImg.setImageResource(R.drawable.upgradecat17);
            newImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int minInput = 1;
            int maxInput = 66;
            int minAlpha = 0;
            int maxAlpha = 255;

            int numberIdInt = Integer.parseInt(numberId);
            int alphaValue = (int) (((numberIdInt - minInput) / (float) (maxInput - minInput)) * (maxAlpha - minAlpha) + minAlpha);

            newImg.setImageAlpha(alphaValue);

        }else{
            changeImg(numberId, newImg);
        }

        newImg.setPadding(dpToPx(10), dpToPx(-10), dpToPx(10), dpToPx(10));
        newImg.setScaleType(ImageView.ScaleType.CENTER_CROP);


        //Título
        TextView newTitle = new TextView(context);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        newTitle.setLayoutParams(titleParams);

        //Título personalizado
        if(AppDataBase.getInstance().loadmode66Preference(getContext())){
            newTitle.setText("😈 " + numberId + "\uD83D\uDC31 ");
        }else{
            LanguageTranslator.getInstance().renameUpgrades(numberId, newTitle);
        }

        newTitle.setTextSize(textSize);
        newTitle.setTypeface(typeface);
        newTitle.setTextColor(Color.parseColor(titleUpgrade));
        newTitle.setGravity(Gravity.START);

        //Nivel
        TextView newLevel = new TextView(context);
        LinearLayout.LayoutParams levelParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        newLevel.setLayoutParams(levelParams);
        newLevel.setText(idUserLevel);
        newLevel.setTextSize(textSize);
        newLevel.setTypeface(typeface);
        newLevel.setTextColor(Color.parseColor(titleUpgrade));
        newLevel.setGravity(Gravity.CENTER);

        //Coste
        TextView newCost = new TextView(context);
        LinearLayout.LayoutParams costParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        newCost.setLayoutParams(costParams);
        newCost.setText(NumberFormatter.formatNumber(cost));
        newCost.setTextSize(textSize);
        newCost.setTypeface(typeface);
        newCost.setTextColor(Color.parseColor(costeffectbutton));
        newCost.setGravity(Gravity.CENTER);

        //Effect
        TextView newEffect = new TextView(context);
        LinearLayout.LayoutParams effectParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        newEffect.setLayoutParams(effectParams);
        if(Objects.equals(upgradeType, "Active"))newEffect.setText(NumberFormatter.formatNumber(effect) + "/ck");
        else if(Objects.equals(upgradeType, "Passive")) newEffect.setText(NumberFormatter.formatNumber(effect) + "/s");
        newEffect.setTextSize(textSize);
        newEffect.setTypeface(typeface);
        newEffect.setTextColor(Color.parseColor(costeffectbutton));
        newEffect.setGravity(Gravity.CENTER);

        //Button
        Button newButton = new MaterialButton(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(0, 0, dpToPx(10),0);
        newButton.setWidth(dpToPx(150));
        newButton.setLayoutParams(buttonParams);
        if(LanguageTranslator.getInstance().getCurrentLanguage() == LanguageTranslator.Language.SPANISH){
            newButton.setText("Mejorar");

        }else{
            newButton.setText("Upgrade");

        }
        newButton.setAllCaps(false);
        newButton.setId(View.generateViewId());
        buttonId = newButton.getId();
        monitorButtonState(newButton);

        newButton.setTextSize(textSize);
        newButton.setTextColor(Color.parseColor("#F7EDE2"));
        newButton.setTypeface(typeface);
        newButton.setGravity(Gravity.CENTER);

        //FUNCINALIDAD
        newButton.setTag(R.id.cost_tag, cost);
        newButton.setTag(R.id.effect_tag, effect);
        newButton.setTag(R.id.idUpgrade_tag, idUpgrade);
        newButton.setTag(R.id.idUserLevel_tag, idUserLevel);
        newButton.setTag(R.id.img_tag, newImg);

        //EL PULSAR
        newButton.setOnClickListener(ButtonUpgrade);

        //añadir

        verticalLayoutImg.addView(newImg);

        horizontalLayoutText.addView(newTitle);
        horizontalLayoutText.addView(newCost);
        horizontalLayoutText.addView(newEffect);
        horizontalLayoutText.addView(newLevel);

        horizontalLayoutButton.addView(newButton);

        verticalLayoutTextButton.addView(horizontalLayoutText);
        verticalLayoutTextButton.addView(horizontalLayoutButton);

        mainLayout.addView(verticalLayoutImg);
        mainLayout.addView(verticalLayoutTextButton);

        // Añadir el nuevo LinearLayout al contenedor
        container.addView(mainLayout, 0);
        Log.d("Clicker -> ", "FormatUI: " + name + ", Id: " + idUserLevel + ", Description: " + description + ", Cost: " + newCost.getText() + ", Effect: " + newEffect.getText());
    }


    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }


//region Gestión de actualizar la compra de mejoras segun el score actualizado
    private final Handler handler = new Handler();
    private void monitorButtonState(Button button) {
        Runnable buttonUpdater = new Runnable() {
            @Override
            public void run() {
                updateButtonState(button); // Pasar la referencia del botón
                handler.postDelayed(this, 500); // Comprobar cada 500 ms
            }
        };

        handler.post(buttonUpdater); // Iniciar el monitoreo
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null); // Detener todos los callbacks
    }
    public void updateButtonState(Button button) {
        double cost = (double) button.getTag(R.id.cost_tag);
        if (userHasEnoughScore(cost)) {
            button.setBackgroundColor(Color.parseColor("#8f2d56"));
        } else {
            button.setBackgroundColor(Color.parseColor("#9b9b9b"));
        }
    }
//endregion

    private boolean userHasEnoughScore(double cost){
        double score = ScoreManager.getInstance().getScore();
        return score >= cost;
    }


    private final View.OnClickListener ButtonUpgrade = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            // Obtener la mejora a partir del tag del botón

            double cost = (double) view.getTag(R.id.cost_tag);
            double effect =  (double)view.getTag(R.id.effect_tag);
            String idUpgrade = (String) view.getTag(R.id.idUpgrade_tag);
            String idUserLevel = (String) view.getTag(R.id.idUserLevel_tag);


            if(!userHasEnoughScore(cost)){
                AnimationManager.getInstance().shakeAnimation(view);
            }else {

                Log.d("Clicker->", "Coste: " + cost + ", Efecto: " + effect);

                //toast
                /*Toast.makeText(
                        context,
                        userHasEnoughScore(cost) ? "Has comprado la mejora" + idUpgrade : "No tienes suficiente score",
                        Toast.LENGTH_SHORT
                ).show();*/

                if (upgradeType.equals("Active"))
                    ScoreManager.getInstance().applyActiveUpgrade(requireContext(), cost, effect);
                else if (upgradeType.equals("Passive"))
                    ScoreManager.getInstance().applyPassiveUpgrade(requireContext(), cost, effect);

                //Poner esa mejora al nivel siguiente
                viewModel.updateUserLevel(idUpgrade, idUserLevel, upgradeType, userId);

                synchronized (container) {
                    //container.removeAllViews();//eliminar las vistas
                    List<View> viewsToRemove = new ArrayList<>();
                    for (int i = 0; i < container.getChildCount(); i++) {
                        View child = container.getChildAt(i);
                        if (child.getId() != R.id.mystery_layout) {
                            viewsToRemove.add(child);
                        }
                    }
                    for (View viewUpgrade : viewsToRemove) {
                        container.removeView(viewUpgrade);
                    }
                }
                viewModel.getUpgradesTypeUserLevel(upgradeType, userId);

                //Animación de caer gatitos
                Game.getInstance().addImage(context, idUserLevel);

            }
        }
    };



    void changeImg(String idUserLevel, ImageView image){
        switch (idUserLevel){
            case "1":
                image.setImageResource(R.drawable.upgradecat1);
                break;
            case  "2":
                image.setImageResource(R.drawable.upgradecat2);
                break;
            case"3":
                image.setImageResource(R.drawable.upgradecat3);
                break;
            case "4":
                image.setImageResource(R.drawable.upgradecat4);
                break;
            case "5":
                image.setImageResource(R.drawable.upgradecat5);
                break;
            case "6":
                image.setImageResource(R.drawable.upgradecat6);
                break;
            case "7":
                image.setImageResource(R.drawable.upgradecat7);
                break;
            case "8":
                image.setImageResource(R.drawable.upgradecat8);
                break;
            case "9":
                image.setImageResource(R.drawable.upgradecat9);
                break;
            case "10":
                image.setImageResource(R.drawable.upgradecat10);
                break;
            case "11":
                image.setImageResource(R.drawable.upgradecat11);
                break;
            case "12":
                image.setImageResource(R.drawable.upgradecat12);
                break;
            case "13":
                image.setImageResource(R.drawable.upgradecat13);
                break;
            case "14":
                image.setImageResource(R.drawable.upgradecat14);
                break;
            case "15":
                image.setImageResource(R.drawable.upgradecat15);
                break;
            case "16":
                 image.setImageResource(R.drawable.upgradecat16);
                 break;
            case "17":
                image.setImageResource(R.drawable.upgradecat17);
                break;
            case "18":
                image.setImageResource(R.drawable.upgradecat18);
                break;
            default:
                image.setImageResource(R.drawable.caticon); // Imagen por defecto
                break;
        }
    }
}




