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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
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
    ProgressBar progressBar;
    Context context;
    LinearLayout container;
    TextView title;
    String userId = "User1";
    String upgradeType;

    String userLevel ="0";
    String idUpgrade ="";

    ImageView image;

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
        Log.d("Clicker->", "UpgradeFragment inflando la vista");
        View rootView = inflater.inflate(R.layout.fragment_upgrades, container, false);
        context = rootView.getContext();
        this.container = rootView.findViewById(R.id.container);
        ImageButton buttonBack = rootView.findViewById(R.id.buttonBack);
        title = rootView.findViewById(R.id.title);
        progressBar = rootView.findViewById(R.id.progressBar);



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
                AnimationManager.getInstance().moveLayoutButtons(Game.getInstance().horizontalFlech, Game.getInstance().isFragmentOpen);
            }
        });
        //Boton de atras nativo
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Game.getInstance().isFragmentOpen = false;
                AnimationManager.getInstance().moveLayoutButtons(Game.getInstance().horizontalFlech, Game.getInstance().isFragmentOpen);

                if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    requireActivity().finish();
                }
            }
        });

        // Mostrar algo básico si no hay datos
        assert getArguments() != null;
       upgradeType = getArguments().getString(ARG_UPGRADE_TYPE);
       if(upgradeType.equals("Active")) title.setText("Mejoras activas");
       else if(upgradeType.equals("Passive")) title.setText("Mejoras pasivas");

        //FormatUI("Name", "Description", "Id", 0, 0);

        //Obtener todas las mejoras del tipo que sea y meterlas en filterdUpgrades
        //upgradeType te lo da el boton al pulsar y userID lo hardcodeo

        viewModel.pollo.observe(getViewLifecycleOwner(), pollitos-> {
            //CUando este valor cambie, el codigo de aqui se ejecutará
            Log.d("Pollo", "Pollo ha cambiado a :" + pollitos);
                });

        progressBar.setVisibility(View.VISIBLE);

        viewModel.filteredUpgrades.observe(getViewLifecycleOwner(), upgrades -> {
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
        verticalLayoutImg.setBackgroundColor(Color.parseColor("#F7EDE2"));

        //Layout vertical para el texto y el botón
        LinearLayout verticalLayoutTextButton = new LinearLayout(context);
        verticalLayoutTextButton.setLayoutParams(new LinearLayout.LayoutParams(
                0, // Ancho proporcional
                LinearLayout.LayoutParams.WRAP_CONTENT,
                4f
        ));
        verticalLayoutTextButton.setOrientation(LinearLayout.VERTICAL);
        verticalLayoutTextButton.setPadding(0, dpToPx(10), 0, dpToPx(0));
        verticalLayoutTextButton.setBackgroundColor(Color.parseColor("#F7EDE2"));


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
        changeImg(numberId, newImg);

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
        renameUpgrades(numberId, newTitle);

        newTitle.setTextSize(textSize);
        Typeface typefaceTitle = ResourcesCompat.getFont(context, R.font.cleanow);
        newTitle.setTypeface(typefaceTitle);
        newTitle.setTextColor(ContextCompat.getColor(context, R.color.black));
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
        Typeface typefaceLevel = ResourcesCompat.getFont(context, R.font.cleanow);
        newLevel.setTypeface(typefaceLevel);
        newLevel.setTextColor(Color.BLACK);
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
        Typeface typefaceCost = ResourcesCompat.getFont(context, R.font.cleanow);
        newCost.setTypeface(typefaceCost);
        newCost.setTextColor(Color.parseColor("#8f2d56"));
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
        Typeface typefaceEffect = ResourcesCompat.getFont(context, R.font.cleanow);
        newEffect.setTypeface(typefaceEffect);
        newEffect.setTextColor(Color.parseColor("#8f2d56"));
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
        newButton.setText("mejorar");
        newButton.setAllCaps(false);

        if(userHasEnoughScore(cost)) {
            newButton.setBackgroundColor(Color.parseColor("#8f2d56"));

        }else{
            newButton.setBackgroundColor(Color.parseColor("#9b9b9b"));
        }

        newButton.setTextSize(textSize);
        newButton.setTextColor(Color.parseColor("#F7EDE2"));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.glina_script);
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
                    container.removeAllViews();//eliminar las vistas
                }
                viewModel.getUpgradesTypeUserLevel(upgradeType, userId);

                //Animación de caer gatitos
                Game.getInstance().addImage(context, idUserLevel);

            }
        }
    };



    private boolean userHasEnoughScore(double cost){
        double score = ScoreManager.getInstance().getScore();
        return score >= cost;
    }

    void renameUpgrades(String idUserLevel, TextView textView) {
        switch (idUserLevel) {
            case "1":
                textView.setText("Carmín");
                break;
            case "2":
                textView.setText("Lavanda");
                break;
            case "3":
                textView.setText("Celeste");
                break;
            case "4":
                textView.setText("Aguamarina");
                break;
            case "5":
                textView.setText("Menta");
                break;
            case "6":
                textView.setText("Áureo");
                break;
            case "7":
                textView.setText("Amatista");
                break;
            case "8":
                textView.setText("Lirios");
                break;
            case "9":
                textView.setText("Cobalto");
                break;
            case "10":
                textView.setText("Bosque");
                break;
            case "11":
                textView.setText("Oliva");
                break;
            case "12":
                textView.setText("Siena");
                break;
            case "13":
                textView.setText("Burdeos");
                break;
            case "14":
                textView.setText("Mango");
                break;
            case "15":
                textView.setText("Plata");
                break;
            case "16":
                textView.setText("Dorado");
                break;
            case "17":
                textView.setText("Blanco");
                break;
            case "18":
                textView.setText("Ébano");
                break;
            default:
                textView.setText("Gatito");
                break;
        }
    }

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




