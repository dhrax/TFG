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
import com.badlogic.gdx.utils.Array;

public class ElegirPersonaje implements Screen, InputProcessor {
    Juego juego;

    Array<TextureRegion> regionArray = new Array<>();
    int mostrando;
    TextureRegion flechaDerecha, flechaIzquierda;
    Rectangle rectFlechaDerecha, rectFlechaIzquierda;
    OrthographicCamera cam;

    public ElegirPersonaje(Juego juego) {
        this.juego = juego;
        inicializar();
        mostrando = 0;

        flechaDerecha = new TextureRegion(new Sprite(new Texture(Gdx.files.internal("flecha_derecha_prueba.png"))));
        flechaIzquierda = new TextureRegion(new Sprite(new Texture(Gdx.files.internal("flecha_derecha_prueba.png"))));

        flechaIzquierda.flip(true, false);

        rectFlechaDerecha = new Rectangle(350, 400, 100, 100);
        rectFlechaIzquierda = new Rectangle(950, 400, 100, 100);


        Gdx.input.setInputProcessor(this);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


    }

    private void inicializar() {
        for (int i = 1; i < 7; i++) {
            regionArray.add(new Sprite(new Texture(Gdx.files.internal("nave_prueba_" + i + ".png"))));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        actualizar(delta);
        pintar();

    }

    private void actualizar(float delta) {


    }

    private void pintar() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        juego.batch.begin();
        juego.batch.draw(flechaIzquierda, 350, 400);
        if(mostrando != 0)
            juego.batch.draw(regionArray.get(mostrando - 1), 500, 400);
        juego.batch.draw(regionArray.get(mostrando), 650, 400);
        if(mostrando != 5)
            juego.batch.draw(regionArray.get(mostrando + 1), 800, 400);
        juego.batch.draw(flechaDerecha, 950, 400);
        juego.batch.end();

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


        Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        //cam.unproject(tmp);

        /*Gdx.app.debug("X", String.valueOf(screenX));
        Gdx.app.debug("Y", String.valueOf(screenY));
        Gdx.app.debug("INPUT X", String.valueOf(Gdx.input.getX()));
        Gdx.app.debug("INPUT Y", String.valueOf(Gdx.input.getY()));

        Gdx.app.debug("Y REAL", String.valueOf(Gdx.graphics.getHeight() - tmp.y));*/


        if (rectFlechaDerecha.contains(tmp.x, Gdx.graphics.getHeight() - tmp.y)) {

            if(mostrando > 0){
                mostrando--;
            }
            Gdx.app.debug("TOCADO", String.valueOf(mostrando));

        }else if(rectFlechaIzquierda.contains(tmp.x, Gdx.graphics.getHeight() - tmp.y)){

            if(mostrando < regionArray.size-1){
                mostrando++;
            }
            Gdx.app.debug("TOCADO", String.valueOf(mostrando));

        }

        return true;
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
