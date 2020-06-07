package com.daisa.tfg;

public class UtilAndroid {

    public static String NOMBRE_DISPOSITIVO = "nombre de dispositivo";

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
