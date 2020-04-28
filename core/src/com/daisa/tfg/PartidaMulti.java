package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PartidaMulti implements Screen, InputProcessor {

    Juego juego;
    Personaje personaje;
    float accelX, accelY;

    Stage stage;

    OrthographicCamera camera;
    ExtendViewport viewport;

    public PartidaMulti(Juego juego, TextureRegion textura) {
        this.juego = juego;
        Gdx.app.debug("DEBUG", "Pantalla: [" + ConstantesJuego.ANCHO_PANTALLA_MULTI + ", " + ConstantesJuego.ALTO_PANTALLA_MULTI + "]");

        camera= new OrthographicCamera(ConstantesJuego.ANCHO_PANTALLA_MULTI, ConstantesJuego.ALTO_PANTALLA_MULTI);
        viewport = new ExtendViewport(ConstantesJuego.ANCHO_PANTALLA_MULTI, ConstantesJuego.ALTO_PANTALLA_MULTI, ConstantesJuego.ANCHO_PANTALLA_MULTI, ConstantesJuego.ALTO_PANTALLA_MULTI);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(this);
        personaje = new Personaje(textura, 5, 3);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        actualizar();
        pintar();
    }

    private void actualizar() {
        moverPersonaje();
        moverBalas();
        comprobarColisiones();
        comprobarLimites();
        //Gdx.app.debug("DEBUG", "Posicion: [" + personaje.getPosicion().x + ", " + personaje.getPosicion().y + "]");
    }

    private void moverPersonaje() {
        accelX = Gdx.input.getAccelerometerX();
        if(accelX>0){
            personaje.moverAbajo();
        }else{
            personaje.moverArriba();
        }


        accelY = Gdx.input.getAccelerometerY();
        if(accelY>0){
            personaje.moverDerecha();
        }else{
            personaje.moverIzquierda();
        }
        //personaje.setPosicion(new Vector2(personaje.getPosicion().x+accelY, personaje.getPosicion().y));
        //personaje.setPosicion(new Vector2(personaje.getPosicion().x, personaje.getPosicion().y-accelX));
    }

    private void moverBalas() {

        for(Bala bala : personaje.balas){
            bala.moverArriba();
        }

        for(Bala balaRival : personaje.balasRival){
            balaRival.moverAbajo();
        }
    }


    private void comprobarColisiones() {
    }

    private void comprobarLimites() {

        if(personaje.getPosicion().x < 0)
            personaje.getPosicion().x = 0;
        else if(personaje.getPosicion().x > Gdx.graphics.getWidth())
            personaje.getPosicion().x = Gdx.graphics.getWidth();

        if(personaje.getPosicion().y < 0)
            personaje.getPosicion().y = 0;
        else if(personaje.getPosicion().y > Gdx.graphics.getHeight())
            personaje.getPosicion().y = Gdx.graphics.getHeight();

        for(Bala bala : personaje.balas){
            if(bala.getPosicion().y >= ConstantesJuego.ALTO_PANTALLA_MULTI){
                juego.write(bala.posicion.x + ":" + 1);
                personaje.balas.removeValue(bala, false);
            }

        }
    }

    private void pintar() {
        camera.update();
        juego.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        juego.batch.begin();
        personaje.pintar(juego.batch);
        for(Bala bala : personaje.balas)
            bala.pintar(juego.batch);
        for(Bala balaRival : personaje.balasRival){
            balaRival.pintar(juego.batch);
        }
        juego.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        Gdx.app.debug("DEBUG", "Se ha disparado una bala");
        personaje.disparar();
        return true;
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
