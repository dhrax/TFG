package com.daisa.tfg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class ElegirPersonajee implements Screen, InputProcessor {
    Juego juego;

    Array<Image> regionArray = new Array<>();
    int mostrando;

    Stage stage;

    public ElegirPersonajee(Juego juego) {
        this.juego = juego;
        inicializar();
        mostrando = 0;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {

            }

            @Override
            public void onRight() {
                if(mostrando > 0)
                    mostrando--;

                Gdx.app.debug("Desliza Der", String.valueOf(mostrando));
                show();
            }

            @Override
            public void onLeft() {
                if(mostrando < regionArray.size-1)
                    mostrando++;

                Gdx.app.debug("Desliza Izq", String.valueOf(mostrando));
                show();
            }

            @Override
            public void onDown() {

            }
        }));
    }

    private void inicializar() {
        for (int i = 1; i < 11; i++) {
            regionArray.add(new Image(new Texture(Gdx.files.internal("Personajes/p"+i+".png"))));
        }
    }

    @Override
    public void show() {
        stage = new Stage(juego.viewport);

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);

        regionArray.get(mostrando).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //SE EMPIEZA LA PARTIDA
            }
        });

        if(mostrando > 0){
            tabla.add(regionArray.get(mostrando - 1)).width(400).height(400).padRight(50);
        }else{
            tabla.add().width(400).height(400).padRight(50);
        }

        tabla.add(regionArray.get(mostrando)).width(700).height(700).padRight(50).padLeft(50);

        if(mostrando < regionArray.size-1){
            tabla.add(regionArray.get(mostrando + 1)).width(400).height(400).padLeft(50);
        }else{
            tabla.add().width(400).height(400).padLeft(50);
        }
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
