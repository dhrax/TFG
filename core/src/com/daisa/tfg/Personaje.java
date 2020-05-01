package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Personaje{

    Rectangle rect;
    int vida;
    float velocidad;
    static float velocidadBalas;
    private Vector2 posicion;
    EstadosPersonaje estado;
    float stateTime;

    Array<Bala> balas;
    static Array<Bala> balasRival;
    static TextureRegion aspectoBala;
    TextureRegion aspectoActual;
    Animation animacionDerecha, animacionIzquierda;
    Array<TextureRegion> texturasDerecha = new Array<>(), texturasIzquierda = new Array<>();
    TextureRegion aspectoBasico;

    float relacionAspecto;
    float anchoRelativoAspecto, altoRelativoAspecto;


    public Personaje(Array<String> rutaAnimaciones, int vida, float velocidad) {
        this.vida = vida;
        this.velocidad = velocidad;
        velocidadBalas = this.velocidad * 3;
        inicializarAnimaciones(rutaAnimaciones);

        estado = EstadosPersonaje.QUIETO;

        relacionAspecto = (float) aspectoBasico.getRegionWidth() / aspectoBasico.getRegionHeight();

        posicion = new Vector2(0, 0);

        anchoRelativoAspecto = ConstantesJuego.PPU * 3 * relacionAspecto;
        altoRelativoAspecto = ConstantesJuego.PPU * 3;

        rect = new Rectangle(posicion.x, posicion.y, anchoRelativoAspecto , altoRelativoAspecto);

        balas = new Array<>();
        balasRival = new Array<>();

        aspectoBala = new TextureRegion(new Sprite(new Texture(Gdx.files.internal("Balas/bomb.png"))));
    }

    private void inicializarAnimaciones(Array<String> rutaAnimaciones) {

        aspectoBasico = new Sprite(new Texture(Gdx.files.internal(rutaAnimaciones.get(0))));

        for(int i = 1; i < 6; i++){
            if(rutaAnimaciones.get(i).contains("D")){
                texturasDerecha.add(new Sprite(new Texture(Gdx.files.internal(rutaAnimaciones.get(i)))));
            }else{
                texturasIzquierda.add(new Sprite(new Texture(Gdx.files.internal(rutaAnimaciones.get(i)))));
            }
        }

        animacionDerecha = new Animation(0.75f, texturasDerecha);
        animacionIzquierda = new Animation(0.75f, texturasIzquierda);

    }

    public static void anadirBalaRival(float balaX) {
        float posicionRelativaRecibido = ConstantesJuego.ANCHO_PANTALLA * balaX / 100;
        Bala balaRival = new Bala(new Vector2(posicionRelativaRecibido, ConstantesJuego.ALTO_PANTALLA), aspectoBala, velocidadBalas);
        balasRival.add(balaRival);
    }

    public int getvida() {
        return vida;
    }

    public void setvida(int vida) {
        this.vida = vida;
    }

    public float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2 posicion) {
        this.posicion = posicion;
    }

    public void mover(Vector2 direccion) {
        posicion.add(direccion.scl(velocidad));
        rect.setPosition(posicion);
    }

    public void moverDerecha(){
        estado = EstadosPersonaje.DERECHA;
        mover(new Vector2(1, 0));
    }

    public void moverIzquierda(){
        estado = EstadosPersonaje.IZQUIERDA;
        mover(new Vector2(-1, 0));
    }

    public void moverArriba(){
        estado = EstadosPersonaje.QUIETO;
        mover(new Vector2(0, 1));
    }

    public void moverAbajo(){
        estado = EstadosPersonaje.QUIETO;
        mover(new Vector2(0, -1));
    }

    public void pintar(SpriteBatch batch){
        batch.draw(aspectoActual, getPosicion().x, getPosicion().y, anchoRelativoAspecto, altoRelativoAspecto);
    }

    public void disparar() {
        Bala bala = new Bala(new Vector2(posicion.x + anchoRelativoAspecto/2f, posicion.y + altoRelativoAspecto), aspectoBala, velocidadBalas);
        balas.add(bala);
    }

    public void actualizarFrame(float delta){
        stateTime += delta;
        switch (estado){

            case DERECHA:
                aspectoActual = (TextureRegion) animacionDerecha.getKeyFrame(stateTime, false);
                break;
            case IZQUIERDA:
                aspectoActual = (TextureRegion) animacionIzquierda.getKeyFrame(stateTime, false);
                break;
            default:
                aspectoActual = aspectoBasico;
                break;
        }

    }

    public enum EstadosPersonaje{
        DERECHA, IZQUIERDA, QUIETO, MUERTO
    }
}
