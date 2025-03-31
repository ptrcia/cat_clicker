package com.example.android_app;
import java.util.Locale;

//formatear los numeros que se muestran en pantalla para que nosea un caos visual y no me estroee la UI
public class NumberFormatter {

    public static String formatNumber(double number) {
        if(number<1000){
            //return String.valueOf(number);
            return String.format(Locale.US, "%.2f", number);
        }

        final String[] letters = new String[]{"", "K", "M", "B", "T"};
        //        final String[] letters = new String[]{"", "K", "M", "B", "T"};
        int index = 0;

        double result = number;
        while (result >= 1000 && index < letters.length-1) {
            result /= 1000;
            index++;
        }

        //redondear
        String formattedResult = String.format(Locale.US, "%.3f", result);

        return formattedResult + letters[index];
         //return  String.format(Locale.US, "%.3f%s", result, letters[index]);

    }
}

