package com.example.android_app;

import android.widget.Button;

public class LanguageTranslator {
    //Menú
    Button buttonStart = MainActivity.getInstance().findViewById(R.id.buttonStart);
    Button buttonExit = MainActivity.getInstance().findViewById(R.id.buttonExit);
    Button butttonContinue = MainActivity.getInstance().findViewById(R.id.buttonContinue);

    //Game
    Button buttonPassives = Game.getInstance().findViewById(R.id.buttonPassives);
    Button buttonActives = Game.getInstance().findViewById(R.id.buttonActives);


    private static LanguageTranslator instance;
    private String currentLanguage = "Spanish"; // Idioma predeterminado

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
            Translate(currentLanguage);
        }else{
            currentLanguage = "Spanish";
            Translate(currentLanguage);
        }
    }

    private void Translate(String currentLanguage){
        if(currentLanguage.equals("Spanish")){
            buttonStart.setText("Nueva partida");
            buttonExit.setText("Salir");
           butttonContinue.setText("Continuar");

            buttonPassives.setText("Mejoras Pasivas");
            buttonActives.setText("Mejoras Activas");
        }else{
            buttonStart.setText("New game");
            buttonExit.setText("Exit");
           butttonContinue.setText("Continue");

            buttonPassives.setText("Passives Upgrades");
            buttonActives.setText("Actives Upgrades");
        }
    }
}


