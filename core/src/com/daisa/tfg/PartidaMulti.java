package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PartidaMulti implements Screen, InputProcessor {

    Juego juego;
    Personaje personaje;
    float accelX, accelY;

    OrthographicCamera camera;

    public PartidaMulti(Juego juego, Array<String> rutaTexturas) {
        this.juego = juego;

        camera= new OrthographicCamera(ConstantesJuego.ANCHO_UNIDADES, ConstantesJuego.ALTO_UNIDADES);
        camera.position.set(ConstantesJuego.ANCHO_UNIDADES / 2, ConstantesJuego.ALTO_UNIDADES / 2, 0);
        camera.update();

        Gdx.input.setInputProcessor(this);
        personaje = new Personaje(rutaTexturas, 5, ConstantesJuego.PPU / 4);
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
        moverPersonaje(delta);
        moverBalas();
        comprobarColisiones();
        comprobarLimites();
    }

    private void moverPersonaje(float delta) {
        accelX = Gdx.input.getAccelerometerX();
        accelY = Gdx.input.getAccelerometerY();

        if(accelX > +1){
            personaje.moverAbajo();
        }
        if (accelX < -1){
            personaje.moverArriba();
        }

        if(accelY > +1){
            personaje.moverDerecha();
        }
        if (accelY < -1){
            personaje.moverIzquierda();
        }
        //personaje.setPosicion(new Vector2(personaje.getPosicion().x+accelY, personaje.getPosicion().y));
        //personaje.setPosicion(new Vector2(personaje.getPosicion().x, personaje.getPosicion().y-accelX));

        personaje.actualizarFrame(delta);
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

        for(Bala bala : personaje.balas){
            for(Bala balaRival : personaje.balasRival){
                if(balaRival.rect.overlaps(bala.rect)){
                    personaje.balasRival.removeValue(balaRival, false);
                    personaje.balas.removeValue(bala, false);
                }
            }
        }

        for(Bala balaRival : personaje.balasRival){
            if(balaRival.rect.overlaps(personaje.rect)){
                personaje.balasRival.removeValue(balaRival, false);
                personaje.vida--;
                if(personaje.vida <= 0){
                    //TODO terminar la partida y añadir el marcador (pantalla de seleccion de personaje)
                    //TODO notificar de partida acabada al rival
                }
            }
        }
    }

    private void comprobarLimites() {

        if(personaje.getPosicion().x < 0)
            personaje.getPosicion().x = 0;
        if(personaje.getPosicion().x > ConstantesJuego.ANCHO_PANTALLA - personaje.anchoRelativoAspecto)
            personaje.getPosicion().x = ConstantesJuego.ANCHO_PANTALLA - personaje.anchoRelativoAspecto;

        if(personaje.getPosicion().y < 0)
            personaje.getPosicion().y = 0;
        if(personaje.getPosicion().y > ConstantesJuego.ALTO_PANTALLA - personaje.altoRelativoAspecto)
            personaje.getPosicion().y = ConstantesJuego.ALTO_PANTALLA - personaje.altoRelativoAspecto;

        for(Bala bala : personaje.balas){
            if(bala.getPosicion().y >= ConstantesJuego.ALTO_PANTALLA){
                float posicionRelativaEnvio = 100 - bala.posicionRelativaPantallaEnvio();
                juego.write(posicionRelativaEnvio + ":" + 1);
                personaje.balas.removeValue(bala, false);
            }
        }
    }

    private void pintar() {
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
