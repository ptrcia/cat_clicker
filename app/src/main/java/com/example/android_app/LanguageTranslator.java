package com.example.android_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class LanguageTranslator {
    //Menú
    Button buttonStart;
    Button buttonExit;
    Button butttonContinue;

    //Game
    Button buttonPassives;
    Button buttonActives;
    Context context;
    public void initialize(Context context) {
        this.context = context;
    }


    // Obtener referencias a los TextViews del fragmento
    UpgradeFragment upgradeFragment = (UpgradeFragment) MainActivity.getInstance()
            .getSupportFragmentManager()
            .findFragmentByTag("UPGRADE_FRAGMENT_TAG");

    private static LanguageTranslator instance;
    private Language currentLanguage = Language.ENGLISH; //por defaults
    public enum Language {
        SPANISH, ENGLISH
    }
    public static synchronized LanguageTranslator getInstance() {
        if (instance == null) {
            instance = new LanguageTranslator();
        }
        return instance;
    }
    public Language getCurrentLanguage() {
        return currentLanguage;
    }
    public void initializeButtons() {
        buttonStart = MainActivity.getInstance().findViewById(R.id.buttonStart);
        buttonExit = MainActivity.getInstance().findViewById(R.id.buttonExit);
        butttonContinue = MainActivity.getInstance().findViewById(R.id.buttonContinue);

        if (Game.getInstance() != null) {
            buttonPassives = Game.getInstance().findViewById(R.id.buttonPassives);
            buttonActives = Game.getInstance().findViewById(R.id.buttonActives);
        }
    }

    public void SelectLanguage() {
        currentLanguage = (currentLanguage == Language.SPANISH) ? Language.ENGLISH : Language.SPANISH;
        Log.d("LanguageTranslator", "Idioma seleccionado: " + currentLanguage );
        Translate(context, currentLanguage);
        saveLanguagePreference(currentLanguage);
    }

    public void Translate(Context context, Language currentLanguage){
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(Game.getInstance() != null){
                buttonPassives.setText("Passive Upgrades");
                buttonActives.setText("Active Upgrades");
            }
            buttonStart.setText("Nueva partida");
            buttonExit.setText("Salir");
            butttonContinue.setText("Continuar");
        }else if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(currentLanguage == Language.SPANISH){
                if (Game.getInstance() != null) {
                    buttonActives.setText("A\nc\ti\nv\na\ns");
                    buttonPassives.setText("P\na\ts\ni\nv\na\ns");
                }
                buttonStart.setText("Nueva partida");
                buttonExit.setText("Salir");
                butttonContinue.setText("Continuar");
            }else{
                if (Game.getInstance() != null) {
                    buttonActives.setText("A\nc\nt\ni\nv\ne\n");
                    buttonPassives.setText("P\na\ns\ns\ni\nv\ne\n");
                }
                buttonStart.setText("New game");
                buttonExit.setText("Exit");
                butttonContinue.setText("Continue");
            }
        }
    }
    public String[] getDialogTexts() {
        if (currentLanguage == Language.SPANISH) {
            return new String[]{
                    "Realizado por: Patricia S. Gracia Artero",
                    "Puedes consultar mi porfolio para más proyectos. Gracias por jugar.",
                    "Abrir porfolio en el navegador",
                    "Atrás"
            };
        } else { // English
            return new String[]{
                    "Created by: Patricia S. Gracia Artero",
                    "You can check out my portfolio for more projects. Thank you for playing.",
                    "Open portfolio in browser",
                    "Back"
            };
        }
    }
    void renameUpgrades(String idUserLevel, TextView textView) {
        if(currentLanguage == Language.SPANISH) {
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
        }else{
            switch (idUserLevel) {
                case "1":
                    textView.setText("Carmine");
                    break;
                case "2":
                    textView.setText("Lavender");
                    break;
                case "3":
                    textView.setText("Celeste");
                    break;
                    case "4":
                    textView.setText("Aquamarine");
                    break;
                case "5":
                    textView.setText("Mint");
                    break;
                case "6":
                    textView.setText("Golden");
                    break;
                case "7":
                    textView.setText("Amethyst");
                    break;
                case "8":
                    textView.setText("Lilies");
                    break;
                    case "9":
                    textView.setText("Cobalt");
                    break;
                case "10":
                    textView.setText("Forest");
                    break;
                case "11":
                    textView.setText("Olive");
                    break;
                case "12":
                    textView.setText("Sienna");
                    break;
                case "13":
                    textView.setText("Burgundy");
                    break;
                    case "14":
                    textView.setText("Mango");
                    break;
                case "15":
                    textView.setText("Silver");
                    break;
                case "16":
                    textView.setText("Golden");
                    break;
                case "17":
                    textView.setText("White");
                    break;
                case "18":
                    textView.setText("Ebony");
                    break;
                    default:
                    textView.setText("Kitty");
                    break;
            }
        }

    }

    public void renameFragment(String type, TextView title, TextView cost, TextView effect, TextView level) {
        if (currentLanguage == Language.SPANISH) {
            if(type.equals("Passive"))title.setText("Mejoras pasivas");
            else{title.setText("Mejoras activas");}
            cost.setText("Coste");
            effect.setText("Efecto");
            level.setText("Nivel");
        } else {
            if(type.equals("Passive"))title.setText("Passive Upgrades");
            else{title.setText("Active Upgrades");}
            cost.setText("Cost");
            effect.setText("Effect");
            level.setText("Level");
        }
    }


    public void saveLanguagePreference(Language language) {
        Context context = MainActivity.getInstance(); // Replace with the appropriate context if needed
        SharedPreferences sharedPreferences = context.getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SelectedLanguage", language.toString());
        Log.d("LanguageTranslator", "Idioma guardado: " + language );
        editor.apply();
    }
    public void loadLanguagePreference() {
        Context context = MainActivity.getInstance(); // Replace with the appropriate context if needed
        SharedPreferences sharedPreferences = context.getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE);
        String savedLanguage = sharedPreferences.getString("SelectedLanguage", "ENGLISH"); // Valor predeterminado: "ENGLISH"
        currentLanguage = Language.valueOf(savedLanguage);
        // Convertir la cadena a enum        Log.d("LanguageTranslator", "Idioma cargado: " + currentLanguage );
       // return currentLanguage;

    }

}


