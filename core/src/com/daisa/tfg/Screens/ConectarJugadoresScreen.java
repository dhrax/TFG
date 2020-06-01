package com.daisa.tfg.screens;

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
import com.daisa.tfg.principal.Juego;

public class ConectarJugadoresScreen implements Screen {

    ScrollPane scrollPane;
    Juego juego;
    Skin skin;
    Stage stage;
    List<String> list;
    Array<String> dispositivosConectados;

    public ConectarJugadoresScreen(Juego juego) {
        this.juego = juego;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        dispositivosConectados = new Array<>();
    }

    @Override
    public void show() {

        stage = new Stage(juego.viewport);

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

        TextButton hostButton = new TextButton("Host", skin);
        hostButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                juego.stop();
                juego.habilitarSerDescubierto();
                juego.empezarAEscuchar();
            }
        });
        TextButton listenButton = new TextButton("Descubrir", skin);
        listenButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                juego.stop();
                juego.descubrirDispositivos();
            }
        });

        scrollPane = new ScrollPane(list);

        tabla.row().height(200).width(500);
        tabla.add(scrollPane);
        tabla.row().height(200).width(500);
        tabla.add(button);
        tabla.row().height(200).width(500);
        tabla.add(hostButton);
        tabla.row().height(200).width(500);
        tabla.add(listenButton);

        Gdx.input.setInputProcessor(stage);

    }

    public void refrescarLista(Array<String> dispositivosConectados) {
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

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        juego.viewport.update(width, height);
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
