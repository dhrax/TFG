package com.daisa.tfg.Screens;

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
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.daisa.tfg.Principal.Juego;

public class ConectarJugadoresScreen implements Screen {

    ScrollPane scrollPane;
    Juego juego;
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
        tabla.setBackground(new TiledDrawable(juego.getFondoMenu()));

        list = new List<>(juego.manager.getSkin());
        list.setItems(dispositivosConectados);


        TextButton button = new TextButton("Conectar", juego.manager.getSkin());
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if(list.getItems().size > 0){
                    String clickedItem = list.getSelected();
                    Gdx.app.debug("DISPOSITIVO ESCOGIDO", clickedItem);
                    juego.conectarBluetooth(clickedItem);
                }

            }
        });

        TextButton hostButton = new TextButton("Host", juego.manager.getSkin());
        hostButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                juego.stop();
                juego.habilitarSerDescubierto();
                juego.empezarAEscuchar();
            }
        });
        TextButton listenButton = new TextButton("Descubrir", juego.manager.getSkin());
        listenButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                juego.stop();
                juego.descubrirDispositivos();
            }
        });

        scrollPane = new ScrollPane(list);

        Table tablaScrollPane = new Table();
        Table tablaBotones = new Table();

        tablaScrollPane.row();
        tablaScrollPane.add(scrollPane).height(800).width(750);

        tablaBotones.row();
        tablaBotones.add(button).height(200).width(700);
        tablaBotones.row();
        tablaBotones.add().height(100);
        tablaBotones.row();
        tablaBotones.add(hostButton).height(200).width(700);
        tablaBotones.row();
        tablaBotones.add().height(100);
        tablaBotones.row();
        tablaBotones.add(listenButton).height(200).width(700);

        tabla.row();
        tabla.add(tablaScrollPane);
        tabla.add().width(250);
        tabla.add(tablaBotones);

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
