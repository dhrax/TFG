package com.daisa.tfg;

import com.badlogic.gdx.Gdx;

public class ConstantesJuego {

    public static int ALTO_PANTALLA = Gdx.graphics.getHeight();
    public static int ANCHO_PANTALLA = Gdx.graphics.getWidth();

    public static float ALTO_UNIDADES = 20;
    public static float PPU = ALTO_PANTALLA / ALTO_UNIDADES;
    public static float ANCHO_UNIDADES = ANCHO_PANTALLA / PPU;
}
