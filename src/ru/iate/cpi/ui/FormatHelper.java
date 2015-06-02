package ru.iate.cpi.ui;

import java.util.Locale;

/**
 * Created by sanea on 02.06.15.
 */
public class FormatHelper {
    public static String GetFloat(float valueToFormat){
        return String.format(Locale.US, "%.4f", valueToFormat);
    }
}
