package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoginScreen implements Screen {


	Juego juego;
	public LoginScreen (Juego juego) {


		this.juego = juego;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		juego.setScreen(new ElegirPersonaje(juego));

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
	public void dispose () {

	}
}
