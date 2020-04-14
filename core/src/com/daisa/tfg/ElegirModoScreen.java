package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ElegirModoScreen implements Screen {

    Stage stage;
    Juego juego;

    Skin skin;

    TextButton btJugar;
    TextButton btRanking;
    TextButton btAjustes;

    public ElegirModoScreen(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        stage = new Stage();

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);

        skin = juego.manager.managerJuego.get("skin/glassy-ui.json");

        btJugar = new TextButton("Modo Solitario", skin);
        btJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new ElegirPersonaje(juego));
            }
        });


        btRanking = new TextButton("Cooperativo", skin);
        btRanking.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO hacer emparejamiento Bluetooth
            }
        });

        btAjustes = new TextButton("Tutorial", skin);
        btAjustes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO crear un tutorial
            }
        });

        tabla.row().padBottom(30).width(700).height(120);
        tabla.add(btJugar).width(700).height(120);

        tabla.row().padBottom(30).width(700).height(120);
        tabla.add(btRanking);

        tabla.row().padBottom(30).width(700).height(120);
        tabla.add(btAjustes);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
    }
}
