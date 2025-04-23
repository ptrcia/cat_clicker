package com.example.android_app;
import java.util.Locale;

//formatear los numeros que se muestran en pantalla para que nosea un caos visual y no me estroee la UI
public class NumberFormatter {

    public static String formatNumber(double number) {
        if(number<1000){
            if (number == (long) number) {
                return String.format(Locale.US, "%d", (long) number);
            } else {
                return String.format(Locale.US, "%.1f", number);
            }
        }

        final String[] letters = new String[]{"", "K", "M", "B", "T"};
        int index = 0;

        double result = number;
        while (result >= 1000 && index < letters.length-1) {
            result /= 1000;
            index++;
        }

        //redondear
        String formattedResult = String.format(Locale.US, "%.3f", result);

        //quitar decimales si son 0
        if (formattedResult.endsWith(".000")) {
            formattedResult = formattedResult.substring(0, formattedResult.length() - 3);  // Eliminar .000
        } else if (formattedResult.endsWith("0")) {
            formattedResult = formattedResult.substring(0, formattedResult.length() - 1);  // Eliminar el segundo 0
        }
        return formattedResult + letters[index];
    }
}

