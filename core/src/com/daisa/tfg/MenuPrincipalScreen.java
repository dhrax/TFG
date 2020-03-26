package com.daisa.tfg;

import com.badlogic.gdx.Screen;

public class MenuPrincipalScreen implements Screen {

    Juego juego;

    public MenuPrincipalScreen(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        //TODO menu principal
        juego.setScreen(new ElegirPersonaje(juego));

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
