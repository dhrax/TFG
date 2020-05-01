package com.daisa.tfg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//fixme esta clase sera de la que hereden las balas de verdad
public class Bala {

    Vector2 posicion;
    TextureRegion aspecto;
    Rectangle rect;
    //Animation animacion;
    float velocidad;
    float relacionAspecto;
    float anchoRelativoAspecto, altoRelativoAspecto;

    public Bala(Vector2 posicion, TextureRegion aspecto, float velocidad) {
        this.posicion = posicion;
        this.aspecto = aspecto;
        this.velocidad = velocidad;

        relacionAspecto = (float) aspecto.getRegionWidth() / aspecto.getRegionHeight();

        anchoRelativoAspecto = ConstantesJuego.PPU * relacionAspecto;
        altoRelativoAspecto = ConstantesJuego.PPU;

        this.rect = new Rectangle(posicion.x, posicion.y, anchoRelativoAspecto, altoRelativoAspecto);
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
        batch.draw(aspecto, getPosicion().x, getPosicion().y, anchoRelativoAspecto, altoRelativoAspecto);
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2 posicion) {
        this.posicion = posicion;
    }

    public TextureRegion getAspecto() {
        return aspecto;
    }

    public void setAspecto(TextureRegion aspecto) {
        this.aspecto = aspecto;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * @return porcentaje de la posicion en pantalla
     */
    public float posicionRelativaPantallaEnvio() {

        return posicion.x * 100 / ConstantesJuego.ANCHO_PANTALLA;
    }
}
