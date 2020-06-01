package com.daisa.tfg.balas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BalaCirc extends Bala{

    Circle circ;

    public BalaCirc(Vector2 posicion, float velocidad, TextureRegion aspecto, int idPj, int tamanoBala) {
        super(posicion, velocidad, aspecto, idPj, tamanoBala);
        circ= new Circle(posicion.x + getAnchoRelativoAspecto()/2, posicion.y + getAltoRelativoAspecto()/2, getAltoRelativoAspecto()/2);
        Gdx.app.debug("DEBUG", "Posicion Circ Bala: " + circ.toString());
    }
}
