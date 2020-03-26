package com.daisa.tfg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Juego extends Game {

    //TODO Cargar todo los managers aquí
    SpriteBatch batch;
    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new LoginScreen(this));
    }
}
