package com.daisa.tfg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Juego extends Game {

    SpriteBatch batch;
    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new LoginScreen(this));
    }
}
