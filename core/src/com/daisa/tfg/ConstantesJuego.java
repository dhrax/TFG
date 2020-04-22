package com.daisa.tfg;

import com.badlogic.gdx.Gdx;

public class ConstantesJuego {

    public static int ALTURA_UNIDADES = 20;
    public static int ALTO_PANTALLA = Gdx.graphics.getHeight();
    public static int ANCHO_PANTALLA = Gdx.graphics.getWidth();

    public static int PIXELES_POR_UNIDAD_ALTO = ALTO_PANTALLA / ALTURA_UNIDADES;
    public static int PIXELES_POR_UNIDAD_ANCHO = ANCHO_PANTALLA / PIXELES_POR_UNIDAD_ALTO;


}
