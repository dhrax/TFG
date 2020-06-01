package com.daisa.tfg;

import android.app.Activity;
import android.widget.Toast;

import com.daisa.tfg.principal.Juego;

public class UtilAndroid {
    Activity activity;
    Juego juego;

    boolean hayNumerosEnString(String contrasena) {
        int pos = 0;
        boolean esNumero = false;
        do {
            char car = contrasena.charAt(pos);
            try {
                Integer.parseInt(String.valueOf(car));
                esNumero = true;
            } catch (NumberFormatException ignored) {}
            pos++;

        } while (!esNumero && pos < contrasena.length());

        return !esNumero;
    }
}
