package com.daisa.tfg;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PersonajePol extends Personaje {
    Polygon pol;
    float[] arrVertices;
    float polPosX, polPosY;


    public PersonajePol(Array<String> rutaAnimaciones, int idPj, int vida, float velocidad) {
        super(rutaAnimaciones, idPj, vida, velocidad);
        switch (idPj) {
            case 1:
            case 2:
            case 3:
                arrVertices = new float[]{getPosicion().x, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.5f, getPosicion().y + getAltoRelativoAspecto(),
                        getPosicion().x + getAnchoRelativoAspecto(), getPosicion().y};
                break;
            case 7:
                arrVertices = new float[]{getPosicion().x, getPosicion().y + getAltoRelativoAspecto() * 0.6f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.15f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.85f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto(), getPosicion().y + getAltoRelativoAspecto() * 0.6f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.65f, getPosicion().y + getAltoRelativoAspecto(),
                        getPosicion().x + getAnchoRelativoAspecto() * 0.5f, getPosicion().y + getAltoRelativoAspecto() * 0.8f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.35f, getPosicion().y + getAltoRelativoAspecto()};
                break;
            case 8:
                arrVertices = new float[]{getPosicion().x, getPosicion().y + getAltoRelativoAspecto() * 0.5f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.2f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.8f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto(), getPosicion().y + getAltoRelativoAspecto() * 0.5f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.8f, getPosicion().y+ getAltoRelativoAspecto(),
                        getPosicion().x + getAnchoRelativoAspecto() * 0.2f, getPosicion().y+ getAltoRelativoAspecto()};
                break;
            case 9:
                arrVertices = new float[]{getPosicion().x, getPosicion().y + getAltoRelativoAspecto() * 0.5f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.1f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.9f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto(), getPosicion().y + getAltoRelativoAspecto() * 0.5f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.9f, getPosicion().y+ getAltoRelativoAspecto(),
                        getPosicion().x + getAnchoRelativoAspecto() * 0.1f, getPosicion().y+ getAltoRelativoAspecto()};
                break;
            case 10:
                arrVertices = new float[]{getPosicion().x, getPosicion().y + getAltoRelativoAspecto() * 0.5f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.38f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.5f, getPosicion().y + getAltoRelativoAspecto() * 0.22f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.62f, getPosicion().y,
                        getPosicion().x + getAnchoRelativoAspecto(), getPosicion().y + getAltoRelativoAspecto() * 0.5f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.62f, getPosicion().y + getAltoRelativoAspecto(),
                        getPosicion().x + getAnchoRelativoAspecto() * 0.5f, getPosicion().y + getAltoRelativoAspecto() * 0.78f,
                        getPosicion().x + getAnchoRelativoAspecto() * 0.38f, getPosicion().y + getAltoRelativoAspecto()};
                break;
            default:
                break;

        }

        pol = new Polygon(arrVertices);
        pol.setPosition(getPosicion().x, getPosicion().y);
        polPosX=0;
        polPosY=0;
    }

    @Override
    public void mover(Vector2 direccion) {
        getPosicion().add(direccion.scl(getVelocidad()));
        //fixme textura y hitbox no del todo exactos al llegar a la parte derecha de la pantalla
        if(getPosicion().x > 0 && getPosicion().x < ConstantesJuego.ANCHO_PANTALLA - getAnchoRelativoAspecto()){
            for (int i = 0; i < pol.getVertices().length; i+=2) {
                    pol.getVertices()[i] += direccion.x;
            }
            polPosX = getPosicion().x;
        }
        if(getPosicion().y > 0 && getPosicion().y < ConstantesJuego.ALTO_PANTALLA - getAltoRelativoAspecto()){
            for (int i = 1; i < pol.getVertices().length; i+=2) {
                pol.getVertices()[i] += direccion.y;
            }
            polPosY = getPosicion().y;
        }

        pol.setPosition(polPosX, polPosY);
    }
}
