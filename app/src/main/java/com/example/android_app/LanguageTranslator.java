package com.example.android_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;

public class LanguageTranslator {
    //Menú
    Button buttonStart = MainActivity.getInstance().findViewById(R.id.buttonStart);
    Button buttonExit = MainActivity.getInstance().findViewById(R.id.buttonExit);
    Button butttonContinue = MainActivity.getInstance().findViewById(R.id.buttonContinue);

    //Game
    Button buttonPassives;
    Button buttonActives;


    private static LanguageTranslator instance;
    private String currentLanguage;// = "English"; // Idioma predeterminado

    public static synchronized LanguageTranslator getInstance() {
        if (instance == null) {
            instance = new LanguageTranslator();
        }
        return instance;
    }
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void SelectLanguage() {
        if(currentLanguage.equals("Spanish")){
            currentLanguage = "English";

        }else{
            currentLanguage = "Spanish";
        }
        Log.d("LanguageTranslator", "Idioma seleccionado: " + currentLanguage );
        Translate(currentLanguage);
        saveLanguagePreference(currentLanguage);
    }

    public void Translate(String currentLanguage){

        if(currentLanguage.equals("Spanish")){
            if (Game.getInstance() != null) {
                buttonPassives = Game.getInstance().findViewById(R.id.buttonPassives);
                buttonActives = Game.getInstance().findViewById(R.id.buttonActives);
                buttonPassives.setText("Mejoras Pasivas");
                buttonActives.setText("Mejoras Activas");
            }
            buttonStart.setText("Nueva partida");
            buttonExit.setText("Salir");
           butttonContinue.setText("Continuar");


        }else{
            if (Game.getInstance() != null) {
                buttonPassives = Game.getInstance().findViewById(R.id.buttonPassives);
                buttonActives = Game.getInstance().findViewById(R.id.buttonActives);
                buttonPassives.setText("Passive Upgrades");
                buttonActives.setText("Active Upgrades");
            }
            buttonStart.setText("New game");
            buttonExit.setText("Exit");
           butttonContinue.setText("Continue");


        }
    }

    public void saveLanguagePreference(String language) {
        Context context = MainActivity.getInstance(); // Replace with the appropriate context if needed
        SharedPreferences sharedPreferences = context.getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SelectedLanguage", language);
        Log.d("LanguageTranslator", "Idioma guardado: " + language );
        editor.apply();
    }
    public void loadLanguagePreference() {
        Context context = MainActivity.getInstance(); // Replace with the appropriate context if needed
        SharedPreferences sharedPreferences = context.getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE);
        currentLanguage = sharedPreferences.getString("SelectedLanguage", "English"); // Default
        Log.d("LanguageTranslator", "Idioma cargado: " + currentLanguage );
       // return currentLanguage;

    }

}


