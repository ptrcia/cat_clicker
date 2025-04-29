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

    public void SelectLanguage(Context context) {
        currentLanguage = (currentLanguage == Language.SPANISH) ? Language.ENGLISH : Language.SPANISH;
        Log.d("LanguageTranslator", "Idioma seleccionado: " + currentLanguage );
        Translate(context, currentLanguage);
        saveLanguagePreference(currentLanguage);
    }

    public void Translate(Context context, Language currentLanguage){
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(currentLanguage == Language.SPANISH){
                if (Game.getInstance() != null) {
                    buttonActives.setText("Mejoras activas");
                    buttonPassives.setText("Mejoras Pasivas");
                }
                buttonStart.setText("Nueva partida");
                buttonExit.setText("Salir");
                butttonContinue.setText("Continuar");
            }else{
                if (Game.getInstance() != null) {
                    buttonActives.setText("Active Upgrades");
                    buttonPassives.setText("Passive Upgrades");
                }
                buttonStart.setText("New game");
                buttonExit.setText("Exit");
                butttonContinue.setText("Continue");
            }

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

    //region Dialogs
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
     public  String[] getScoredDialog(){
         if (currentLanguage == Language.SPANISH) {
             return new String[]{
                     "¡Hola de nuevo!",
                     "En tu ausencia has acumulado",
                     "puntos."
             };
         } else { // English
             return new String[]{
                     "Welcome back!",
                     "In your absence you have accumulated",
                     "passive.",
             };
         }
     }

     public String[] getFinalDialog(){
         if (currentLanguage == Language.SPANISH) {
             return new String[]{
                     "¡Has comprado todas las activas!",
                     "¡Has comprado todas las pasivas!",
                     "Solo te falta terminar todas las pasivas",
                     "Solo te falta terminar todas las activas",
                     "Modo 99",
                     "Salir",
                     "¡Felicidades! Has comprado todas las mejoras. No lo he puesto fácil pero ¡ahí estás!. Ahora puedes elegirentrte: rendirte aquí y dar el juego por acabado o bien, intentar el modo 99."
             };
         } else { // English
             return new String[]{
                     "You've bought all the actives!",
                     "You've bought all the passives!",
                     "You just need to finish all the passives.",
                     "You just need to finish all the actives.",
                     "Mode 99",
                     "Exit",
                     "Congratulations! You've bought all the upgrades. I didn't make it easy but there you are! Now you have a choice: either give up here and call it a day or try mode 99."
             };
         }
     }
    public String[] getFinal99Dialog(){
        if (currentLanguage == Language.SPANISH) {
            return new String[]{
                    "¿Ya has comprado todas las activas?",
                    "¿Ya has comprado todas las pasivas? ",
                    "Ya te queda menos entonces, ¡Ánimo!",
                    "¿Pero que...-? ¿Felicidades? ¿Cómo es que has sacado tiempo y ganas para esto? " +
                            "Vamos a hacer una cosa. ándame por alguno de estos sitios un mensaje con una captura de este mensaje y la primera que reciba mandaré un regalo. " +
                            "(No puedo hacer más, este juego es gratis y sin muchas pretensiones). " +
                            "Muchísimas, muchísimas gracias."
            };
        } else { // English
            return new String[]{
                    "Have you already purchased all the active ones?",
                    "Have you already purchased all the passive ones?",
                    "You have less time to go then, keep up the good work!",
                    "But what...-? congratulations? how did you find the time and desire for this" + "Let's do something." +
                            "Let's do one thing. send me through one of these sites a message with a screenshot of this message  and the first one I get I'll send a gift. " +
                            "(I can't do more, this game is free and without many pretensions). " +
                            "Thank you very, very much.!"
            };
        }
    }
     //endregion

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


