package com.daisa.tfg.principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.daisa.tfg.balas.Bala;
import com.daisa.tfg.constantes.ConstantesJuego;
import com.daisa.tfg.personajes.Personaje;
import com.daisa.tfg.personajes.PersonajeCirc;
import com.daisa.tfg.personajes.PersonajePol;
import com.daisa.tfg.screens.ElegirPersonaje;
import com.daisa.tfg.util.Explosion;
import com.daisa.tfg.util.FondoAnimado;

public class PartidaMulti implements Screen, InputProcessor {

    Juego juego;
    Personaje personaje;
    float accelX, accelY;
    int duracionPulsacion;

    OrthographicCamera camera;
    private int color;
    FondoAnimado fondoAnimado;

    public PartidaMulti(Juego juego, Array<String> rutaTexturas, int idPJ) {
        this.juego = juego;

        camera = new OrthographicCamera(ConstantesJuego.ANCHO_UNIDADES, ConstantesJuego.ALTO_UNIDADES);
        camera.position.set(ConstantesJuego.ANCHO_UNIDADES / 2, ConstantesJuego.ALTO_UNIDADES / 2, 0);
        camera.update();

        Gdx.input.setInputProcessor(this);
        instanciarPersonaje(idPJ, rutaTexturas);
        duracionPulsacion = 0;

        color = 1;

        fondoAnimado = new FondoAnimado();
    }

    private void instanciarPersonaje(int idPJ, Array<String> rutaTexturas) {
        switch (idPJ) {
            case 1:
            case 2:
            case 3:
            case 7:
            case 8:
            case 9:
            case 10:
                personaje = new PersonajePol(rutaTexturas, idPJ, 5, ConstantesJuego.PPU / 4);
                break;
            case 4:
            case 5:
            case 6:
                personaje = new PersonajeCirc(rutaTexturas, idPJ, 5, ConstantesJuego.PPU / 4);
                break;
            default:
                Gdx.app.error("DEBUG", "[ERROR] Id de personaje no valido");
                break;
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
        moverFondo(delta);
        moverPersonaje(delta);
        recargar();
        comprobarToquePantalla();
        moverBalas();
        comprobarColisiones(delta);
        comprobarLimites();
    }

    private void moverFondo(float delta) {
        fondoAnimado.actualizar(delta);
    }

    private void recargar() {
        if (personaje.getMunicion() < ConstantesJuego.MUNICION_MAXIMA) {
            if (TimeUtils.millis() - personaje.getMomentoUltimaRecarga() >= ConstantesJuego.TIEMPO_REGENERACION_BALAS_MILIS) {
                Gdx.app.debug("DEBUG", "Se regenera una bala, municion actual: " + personaje.getMunicion());
                personaje.setMomentoUltimaRecarga(TimeUtils.millis());
                personaje.setMunicion(personaje.getMunicion() + 1);
            }
        }
    }

    private void comprobarToquePantalla() {
        if (personaje.getIdPj() == 2) {
            if (Gdx.input.isTouched())
                duracionPulsacion++;
        }
    }

    private void moverPersonaje(float delta) {
        accelX = Gdx.input.getAccelerometerX();
        accelY = Gdx.input.getAccelerometerY();

        if ((accelX < -1 || accelX > 1) || (accelY < -1 || accelY > 1)) {
            if (accelX > 1) {
                personaje.moverAbajo();
            }
            if (accelX < -1) {
                personaje.moverArriba();
            }

            if (accelY > 1) {
                personaje.moverDerecha();
            }
            if (accelY < -1) {
                personaje.moverIzquierda();
            }
        } else {
            personaje.setEstado(Personaje.EstadosPersonaje.QUIETO);
        }

        //personaje.setPosicion(new Vector2(personaje.getPosicion().x+accelY, personaje.getPosicion().y));
        //personaje.setPosicion(new Vector2(personaje.getPosicion().x, personaje.getPosicion().y-accelX));

        personaje.actualizarFrame(delta);
    }

    private void moverBalas() {
        personaje.moverBalas(personaje.getBalas());
        personaje.moverBalasRival(Personaje.getBalasRival());
    }


    private void comprobarColisiones(float delta) {
        for (Bala bala : personaje.getBalas()) {
            for (Bala balaRival : Personaje.getBalasRival()) {
                if (bala.comprobarColisiones(bala, balaRival)) {
                    Personaje.getBalasRival().removeValue(balaRival, false);
                    personaje.getBalas().removeValue(bala, false);
                }
            }
        }

        for (Bala balaRival : Personaje.getBalasRival()) {
            if (balaRival.comprobarColisiones(balaRival, personaje)) {
                Personaje.getBalasRival().removeValue(balaRival, false);
                personaje.getArrayExplosiones().add(new Explosion());
                personaje.setVida(personaje.getVida() - balaRival.getTamanoBala());
                if (personaje.getVida() <= 0) {
                    //TODO añadir el marcador (pantalla de seleccion de personaje)
                    //fixme llamar al dispose del rival¿?
                    Gdx.app.debug("DEBUG", "Me han matado");
                    juego.write("fin");
                    juego.setScreen(new ElegirPersonaje(juego));
                    this.dispose();
                }
            }
        }
        for (Explosion explosion : personaje.getArrayExplosiones()) {
            explosion.actualizar(delta);
        }
    }

    private void comprobarLimites() {

        if (personaje.getPosicion().x < 0)
            personaje.getPosicion().x = 0;
        if (personaje.getPosicion().x > ConstantesJuego.ANCHO_PANTALLA - personaje.getAnchoRelativoAspecto())
            personaje.getPosicion().x = ConstantesJuego.ANCHO_PANTALLA - personaje.getAnchoRelativoAspecto();

        if (personaje.getPosicion().y < 0)
            personaje.getPosicion().y = 0;
        if (personaje.getPosicion().y > ConstantesJuego.ALTO_PANTALLA - personaje.getAltoRelativoAspecto())
            personaje.getPosicion().y = ConstantesJuego.ALTO_PANTALLA - personaje.getAltoRelativoAspecto();

        personaje.recolocarHitbox();

        for (Bala bala : personaje.getBalas()) {
            if (bala.getPosicion().y >= ConstantesJuego.ALTO_PANTALLA) {
                float posicionRelativaEnvio = 100 - bala.posicionRelativaPantallaEnvio();
                juego.write(posicionRelativaEnvio + ":" + personaje.getIdPj() + ":" + bala.getTamanoBala());
                personaje.getBalas().removeValue(bala, false);
            }
        }

        for (Bala balaRival : Personaje.getBalasRival()){
            if(balaRival.getPosicion().y <= -balaRival.getAltoRelativoAspecto()){
                Personaje.getBalasRival().removeValue(balaRival, false);
                Gdx.app.debug("DEBUG", "Se borra bala de las balas rivales");
            }
        }
    }

    private void pintar() {
        Gdx.gl.glClearColor(color, color, color, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        juego.batch.begin();
        fondoAnimado.pintar(juego.batch);
        personaje.pintar(juego.batch);
        for (Bala bala : personaje.getBalas())
            bala.pintar(juego.batch);
        for (Bala balaRival : Personaje.getBalasRival()) {
            balaRival.pintar(juego.batch);
        }
        for (Explosion explosion : personaje.getArrayExplosiones()) {
            explosion.pintar(juego.batch, personaje.getPosicion().x + personaje.getAnchoRelativoAspecto()/2, personaje.getPosicion().y + personaje.getAltoRelativoAspecto());
            if (explosion.animacionTerminada){
                personaje.getArrayExplosiones().removeValue(explosion, false);
            }
        }
        personaje.dibujarHud(juego.batch);
        juego.batch.end();

        //Pintarhitbox.pintarHitboxes(personaje);
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
        personaje.dispose();
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
        //Gdx.app.debug("DEBUG", "Se ha disparado una bala");
        //Gdx.app.debug("DEBUG", "Tiempo pulsado: " + duracionPulsacion);
        int tamanoBala = 1;
        if (personaje.getIdPj() == 2) {
            if (duracionPulsacion > 40 && duracionPulsacion <= 80) {
                tamanoBala = 2;
            } else if (duracionPulsacion > 80) {
                tamanoBala = 3;
            }
        }
        //Gdx.app.debug("DEBUG", "Multiplicador tamano: " + tamanoBala);
        personaje.disparar(tamanoBala);
        duracionPulsacion = 0;

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
