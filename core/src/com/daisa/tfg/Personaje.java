package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Personaje{

    TextureRegion aspecto;
    //Animation animacion
    Rectangle rect;
    int vida;
    int velocidad;
    private Vector2 posicion;
    EstadosPersonaje estado;

    Array<Bala> balas;
    static Array<Bala> balasRival;


    public Personaje(TextureRegion aspecto, int vida, int velocidad) {
        this.aspecto = aspecto;
        this.vida = vida;
        this.velocidad = velocidad;

        estado = EstadosPersonaje.QUIETO;

        posicion = new Vector2(0, 0);

        rect = new Rectangle(posicion.x, posicion.y, aspecto.getRegionWidth() , aspecto.getRegionHeight());

        balas = new Array<>();
        balasRival = new Array<>();
    }

    public static void anadirBalaRival(float balaX) {
        TextureRegion aspectoBala = new TextureRegion(new Sprite(new Texture(Gdx.files.internal("Balas/bomb.png"))));
        Bala balaRival = new Bala(new Vector2(balaX, ConstantesJuego.ALTO_PANTALLA_MULTI), aspectoBala, 2);
        balasRival.add(balaRival);
    }

    public int getvida() {
        return vida;
    }

    public void setvida(int vida) {
        this.vida = vida;
    }

    public int getVelocidad() {
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
        mover(new Vector2(1, 0));
    }

    public void moverIzquierda(){
        mover(new Vector2(-1, 0));
    }

    public void moverArriba(){
        mover(new Vector2(0, 1));
    }

    public void moverAbajo(){
        mover(new Vector2(0, -1));
    }
    public void pintar(SpriteBatch batch){
        batch.draw(aspecto, getPosicion().x, getPosicion().y);
    }

    public void disparar() {
        TextureRegion aspectoBala = new TextureRegion(new Sprite(new Texture(Gdx.files.internal("Balas/bomb.png"))));
        Bala bala = new Bala(new Vector2(posicion.x + aspecto.getRegionWidth()/2f, posicion.y), aspectoBala, 2);
        balas.add(bala);
    }

    public enum EstadosPersonaje{
        ARRIBA, ABAJO, DERECHA, IZQUIERDA, QUIETO, MUERTO
    }
}
