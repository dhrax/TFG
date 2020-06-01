package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FondoAnimado {

    public static final int DEFAULT_SPEED = 200;

    TextureRegion image;
    float y1, y2;
    int speed;//In pixels / second
    float imageScale;
    float altoRelativo;

    public FondoAnimado () {
        image = new TextureRegion(new Sprite(new Texture("fondoAnimado.jpg")));

        speed = DEFAULT_SPEED;
        imageScale = (float) ConstantesJuego.ANCHO_PANTALLA / image.getRegionWidth();
        altoRelativo = image.getRegionHeight() * imageScale;

        y1 = 0;
        y2 = altoRelativo;
    }

    public void actualizar (float deltaTime) {
        y1 -= speed * deltaTime;
        y2 -= speed * deltaTime;

        if (y1 + altoRelativo <= 0){
            Gdx.app.debug("DEBUG", "Primer fondo se pone encima");
            y1 = y2 + altoRelativo;
        }

        if (y2 + altoRelativo <= 0){
            Gdx.app.debug("DEBUG", "Segundo fondo se pone encima");
            y2 = y1 + altoRelativo;
        }

    }

    public void pintar(SpriteBatch batch){
        batch.draw(image, 0, y1, ConstantesJuego.ANCHO_PANTALLA, altoRelativo);
        batch.draw(image, 0, y2, ConstantesJuego.ANCHO_PANTALLA, altoRelativo);
    }
}
