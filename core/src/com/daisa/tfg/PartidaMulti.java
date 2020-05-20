package com.daisa.tfg;

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

public class PartidaMulti implements Screen, InputProcessor {

    Juego juego;
    Personaje personaje;
    float accelX, accelY;
    int duracionPulsacion;

    OrthographicCamera camera;

    public PartidaMulti(Juego juego, Array<String> rutaTexturas, int idPJ) {
        this.juego = juego;

        camera = new OrthographicCamera(ConstantesJuego.ANCHO_UNIDADES, ConstantesJuego.ALTO_UNIDADES);
        camera.position.set(ConstantesJuego.ANCHO_UNIDADES / 2, ConstantesJuego.ALTO_UNIDADES / 2, 0);
        camera.update();

        Gdx.input.setInputProcessor(this);
        instanciarPersonaje(idPJ, rutaTexturas);
        duracionPulsacion = 0;
    }

    private void instanciarPersonaje(int idPJ, Array<String> rutaTexturas) {
        switch (idPJ){
            case 1:
            case 2:
            case 3:
            case 7:
            case 8:
            case 9:
            case 10:
                personaje = new PersonajePol(rutaTexturas, idPJ,5, ConstantesJuego.PPU / 4);
                break;
            case 4:
            case 5:
            case 6:
                personaje = new PersonajeCirc(rutaTexturas, idPJ,5, ConstantesJuego.PPU / 4);
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

        //Fixme cambiar de lugar
        if(personaje.getIdPj() == 2){
            if(Gdx.input.isTouched())
                duracionPulsacion++;
        }
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

        if((accelX < -1 || accelX > 1) || (accelY < -1 || accelY > 1)){
            if(accelX > 1){
                personaje.moverAbajo();
            }
            if (accelX < -1){
                personaje.moverArriba();
            }

            if(accelY > 1){
                personaje.moverDerecha();
            }
            if (accelY < -1){
                personaje.moverIzquierda();
            }
        }else{
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


    private void comprobarColisiones() {
        for(Bala bala : personaje.getBalas()){
            for(Bala balaRival : Personaje.getBalasRival()){
                if(bala.comprobarColisiones(bala, balaRival)){
                    Personaje.getBalasRival().removeValue(balaRival, false);
                    personaje.getBalas().removeValue(bala, false);
                }
            }
        }

        for(Bala balaRival : Personaje.getBalasRival()){
            if(balaRival.comprobarColisiones(balaRival, personaje)){
                Personaje.getBalasRival().removeValue(balaRival, false);
                personaje.setVida(personaje.getVida() - 1);
                if(personaje.getVida() <= 0){
                    //TODO añadir el marcador (pantalla de seleccion de personaje)
                    //fixme llamar al dispose del rival¿?
                    Gdx.app.debug("DEBUG", "Me han matado");
                    juego.write("fin");
                    juego.setScreen(new ElegirPersonaje(juego));
                    this.dispose();
                }
            }
        }
    }

    private void comprobarLimites() {

        if(personaje.getPosicion().x < 0)
            personaje.getPosicion().x = 0;
        if(personaje.getPosicion().x > ConstantesJuego.ANCHO_PANTALLA - personaje.getAnchoRelativoAspecto())
            personaje.getPosicion().x = ConstantesJuego.ANCHO_PANTALLA - personaje.getAnchoRelativoAspecto();

        if(personaje.getPosicion().y < 0)
            personaje.getPosicion().y = 0;
        if(personaje.getPosicion().y > ConstantesJuego.ALTO_PANTALLA - personaje.getAltoRelativoAspecto())
            personaje.getPosicion().y = ConstantesJuego.ALTO_PANTALLA - personaje.getAltoRelativoAspecto();

        personaje.recolocarHitbox();

        for(Bala bala : personaje.getBalas()){
            if(bala.getPosicion().y >= ConstantesJuego.ALTO_PANTALLA){
                float posicionRelativaEnvio = 100 - bala.posicionRelativaPantallaEnvio();
                juego.write(posicionRelativaEnvio + ":" + personaje.getIdPj() + ":" + bala.getTamanoBala());
                personaje.getBalas().removeValue(bala, false);
            }
        }
    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    private void pintar() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        juego.batch.begin();
        personaje.pintar(juego.batch);
        for(Bala bala : personaje.getBalas())
            bala.pintar(juego.batch);
        for(Bala balaRival : Personaje.getBalasRival()){
            balaRival.pintar(juego.batch);
        }
        personaje.dibujarHud(juego.batch);

        juego.batch.end();

        Gdx.gl.glLineWidth(5f);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.BLACK);

        for(Bala bala : Personaje.getBalasRival()){
            if(bala instanceof BalaRect){
                Rectangle r = bala.obtenerRectanguloBala(bala);
                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }else if(bala instanceof BalaPol){
                Polygon p = bala.obtenerPoligonoBala(bala);
                shapeRenderer.polygon(p.getVertices());
            }else{
                Circle c = bala.obtenerCirculoBala(bala);
                shapeRenderer.circle(c.x, c.y, c.radius);
            }
        }

        for(Bala bala : personaje.getBalas()){
            if(bala instanceof BalaRect){
                Rectangle r = bala.obtenerRectanguloBala(bala);
                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }else if(bala instanceof BalaPol){
                Polygon p = bala.obtenerPoligonoBala(bala);
                shapeRenderer.polygon(p.getVertices());
            }else{
                Circle c = bala.obtenerCirculoBala(bala);
                shapeRenderer.circle(c.x, c.y, c.radius);
            }
        }

        if(personaje instanceof PersonajeCirc){
            Circle circ = personaje.obtenerCirculo(personaje);
            shapeRenderer.circle(circ.x, circ.y, circ.radius);
        }else{
            Polygon p = personaje.obtenerPoligono(personaje);
            shapeRenderer.polygon(p.getVertices());
        }

        shapeRenderer.end();
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.debug("DEBUG", "Se ha disparado una bala");
        Gdx.app.debug("DEBUG", "Tiempo pulsado: " + duracionPulsacion);
        int tamanoBala = 1;
        if(personaje.getIdPj() == 2){
            if (duracionPulsacion > 40 && duracionPulsacion <= 80){
                tamanoBala = 2;
            }else if (duracionPulsacion > 80){
                tamanoBala = 3;
            }
        }
        Gdx.app.debug("DEBUG", "Multiplicador tamano: " + tamanoBala);
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
