package com.daisa.tfg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class ConectarJugadoresScreen implements Screen {

    ScrollPane scrollPane;
    Juego juego;
    Skin skin;
    Stage stage;
    List<String> list;
    Array<String> dispositivosConectados;

    public ConectarJugadoresScreen(Juego juego) {
        this.juego = juego;
        dispositivosConectados = new Array<>();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        juego.habilitarSerDescubierto();
    }

    @Override
    public void show() {

        stage = new Stage();
        if(!juego.manager.managerJuego.isLoaded(juego.manager.skin)){
            juego.manager.cargaSkin();
            juego.manager.managerJuego.finishLoading();
        }

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);

        skin = juego.manager.managerJuego.get("skin/glassy-ui.json");

        list = new List<>(skin);
        list.setItems(dispositivosConectados);

        TextButton button = new TextButton("Conectar", skin);
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if(list.getItems().size > 0){
                    String clickedItem = list.getSelected();
                    Gdx.app.debug("DISPOSITIVO ESCOGIDO", clickedItem);
                    juego.conectarBluetooth(clickedItem);
                }

            }
        });

        scrollPane = new ScrollPane(list);

        tabla.row().height(200).width(500);
        tabla.add(scrollPane);
        tabla.row().height(200).width(500);
        tabla.add(button);

        Gdx.input.setInputProcessor(stage);

    }

    void refrescarLista(Array<String> dispositivosConectados) {
        this.dispositivosConectados = dispositivosConectados;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                show();
            }
        });
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
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

    }
}
