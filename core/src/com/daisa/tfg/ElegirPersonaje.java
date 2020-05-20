package com.daisa.tfg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class ElegirPersonaje implements Screen, InputProcessor {
    Juego juego;

    Array<Image> regionsPequenos = new Array<>();
    Array<Image> regionGrandes = new Array<>();
    int mostrando;

    Stage stage;
    InputProcessor gestosProcesador;
    boolean esperando;
    Skin skin;
    Array<Array<String>> rutaMatrizAnimaciones = new Array<>();

    public ElegirPersonaje(Juego juego) {
        this.juego = juego;
        inicializar();
        mostrando = 0;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        gestosProcesador = new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {

            }

            @Override
            public void onRight() {
                if (mostrando > 0)
                    mostrando--;

                Gdx.app.debug("Desliza Der", String.valueOf(mostrando));
                show();
            }

            @Override
            public void onLeft() {
                if (mostrando < regionsPequenos.size - 1)
                    mostrando++;

                Gdx.app.debug("Desliza Izq", String.valueOf(mostrando));
                show();
            }

            @Override
            public void onDown() {

            }
        });
        esperando = false;
    }

    private void inicializar() {
        for (int i = 0; i < 10; i++) {
            regionsPequenos.add(new Image(new Texture(Gdx.files.internal("Personajes/Personajes Peques/p" + (i + 1) + ".png"))));
            regionGrandes.add(new Image(new Texture(Gdx.files.internal("Personajes/Personajes Grandes/p" + (i + 1) + ".png"))));
        }

        for (int i = 0; i < 10; i++) {
            rutaMatrizAnimaciones.add(new Array<String>());
            rutaMatrizAnimaciones.get(i).add("Personajes/Animaciones/p" + (i + 1) + ".png");
            for (int j = 0; j < 3; j++) {
                rutaMatrizAnimaciones.get(i).add("Personajes/Animaciones/p" + (i + 1) + "D" + (j + 1) + ".png");
                rutaMatrizAnimaciones.get(i).add("Personajes/Animaciones/p" + (i + 1) + "I" + (j + 1) + ".png");
            }
        }
    }

    @Override
    public void show() {
        stage = new Stage(juego.viewport);

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);
        skin = juego.manager.managerJuego.get("skin/glassy-ui.json");


        regionGrandes.get(mostrando).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!esperando) {
                    //SE EMPIEZA LA PARTIDA
                    juego.comenzarPartida();
                    esperando = true;
                    juego.yoPreparado = true;
                    show();
                }
            }
        });

        if (mostrando > 0) {
            tabla.add(regionsPequenos.get(mostrando - 1)).padRight(50);
        } else {
            tabla.add().width(400).height(400).padRight(50);
        }

        tabla.add(regionGrandes.get(mostrando)).padRight(50).padLeft(50);

        if (mostrando < regionsPequenos.size - 1) {
            tabla.add(regionsPequenos.get(mostrando + 1)).padLeft(50);
        } else {
            tabla.add().width(400).height(400).padLeft(50);
        }

        Label label = new Label("Esperando al rival", skin);
        label.setColor(Color.BLACK);
        if (esperando) {
            tabla.row();
            tabla.add(label);
        }

        //fixme solucionar problema para cambiar a las imagenes al rotarlas
        if (!esperando) {
            InputMultiplexer inputMultiplexer = new InputMultiplexer(gestosProcesador, stage);
            Gdx.input.setInputProcessor(inputMultiplexer);
        }else{
            Gdx.input.setInputProcessor(null);
        }

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (juego.yoPreparado && juego.rivalPreparado) {
            esperando = false;
            juego.yoPreparado = false;
            juego.rivalPreparado = false;
            juego.setScreen(new PartidaMulti(juego, rutaMatrizAnimaciones.get(mostrando), mostrando + 1));
            this.dispose();
        }

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
        regionsPequenos.clear();
        regionGrandes.clear();
        rutaMatrizAnimaciones.clear();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
