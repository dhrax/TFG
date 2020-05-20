package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Field;

public abstract class Bala {

    private Vector2 posicion;
    private TextureRegion aspecto;
    private float velocidad;
    private float relacionAspecto;
    private float anchoRelativoAspecto, altoRelativoAspecto;
    private static final Vector2 centro = new Vector2();
    private static final Vector2 vec1 = new Vector2();
    private static final Vector2 vec2 = new Vector2();
    private int tamanoBala;
    private int idPj;

    public Bala(Vector2 posicion, float velocidad, TextureRegion aspecto, int idPj, int tamanoBala) {
        this.posicion = posicion;
        this.velocidad = velocidad;
        this.aspecto = aspecto;
        this.idPj = idPj;

        relacionAspecto = (float) this.aspecto.getRegionWidth() / this.aspecto.getRegionHeight();

        anchoRelativoAspecto = ConstantesJuego.PPU * relacionAspecto * tamanoBala;
        altoRelativoAspecto = ConstantesJuego.PPU * tamanoBala;

        this.tamanoBala = tamanoBala;
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

    public float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public float getRelacionAspecto() {
        return relacionAspecto;
    }

    public void setRelacionAspecto(float relacionAspecto) {
        this.relacionAspecto = relacionAspecto;
    }

    public float getAnchoRelativoAspecto() {
        return anchoRelativoAspecto;
    }

    public void setAnchoRelativoAspecto(float anchoRelativoAspecto) {
        this.anchoRelativoAspecto = anchoRelativoAspecto;
    }

    public float getAltoRelativoAspecto() {
        return altoRelativoAspecto;
    }

    public void setAltoRelativoAspecto(float altoRelativoAspecto) {
        this.altoRelativoAspecto = altoRelativoAspecto;
    }

    public static Vector2 getCentro() {
        return centro;
    }

    public static Vector2 getVec1() {
        return vec1;
    }

    public static Vector2 getVec2() {
        return vec2;
    }

    public int getTamanoBala() {
        return tamanoBala;
    }

    public void setTamanoBala(int tamanoBala) {
        this.tamanoBala = tamanoBala;
    }

    public int getIdPj() {
        return idPj;
    }

    public void setIdPj(int idPj) {
        this.idPj = idPj;
    }

    public void pintar(SpriteBatch batch) {
        batch.draw(aspecto, posicion.x, posicion.y, anchoRelativoAspecto, altoRelativoAspecto);
    }

    /**
     * @return porcentaje de la posicion en pantalla
     */
    public float posicionRelativaPantallaEnvio() {
        return posicion.x * 100 / ConstantesJuego.ANCHO_PANTALLA;
    }

    public boolean comprobarColisiones(Bala bala, Bala balaRival) {
        if (bala instanceof BalaRect) {
            Rectangle rect = obtenerRectanguloBala(bala);
            if (balaRival instanceof BalaRect) {
                Rectangle rectRival = obtenerRectanguloBala(balaRival);
                if (rect != null && rectRival != null)
                    return rect.overlaps(rectRival);

            } else if (balaRival instanceof BalaCirc) {
                Circle circRival = obtenerCirculoBala(balaRival);
                if (rect != null && circRival != null)
                    return Intersector.overlaps(circRival, rect);

            } else {
                Polygon polRival = obtenerPoligonoBala(balaRival);
                if (rect != null && polRival != null)
                    return chocaRectanguloPoligono(rect, polRival, polRival.getVertices());
            }

        } else if (bala instanceof BalaCirc) {
            Circle circ = obtenerCirculoBala(bala);
            if (balaRival instanceof BalaRect) {
                Rectangle rectRival = obtenerRectanguloBala(balaRival);
                if (circ != null && rectRival != null)
                    return Intersector.overlaps(circ, rectRival);

            } else if (balaRival instanceof BalaCirc) {
                Circle circRival = obtenerCirculoBala(balaRival);
                if (circ != null && circRival != null)
                    return circ.overlaps(circRival);

            } else {
                Polygon polRival = obtenerPoligonoBala(balaRival);
                if (circ != null && polRival != null)
                    return chocaCirculoPoligono(circ, polRival);
            }

        } else if (bala instanceof BalaPol) {
            Polygon pol = obtenerPoligonoBala(bala);
            if (balaRival instanceof BalaRect) {
                Rectangle rectRival = obtenerRectanguloBala(balaRival);
                if (pol != null && rectRival != null)
                    return chocaRectanguloPoligono(rectRival, pol, pol.getVertices());

            } else if (balaRival instanceof BalaCirc) {
                Circle circRival = obtenerCirculoBala(balaRival);
                if (pol != null && circRival != null)
                    return chocaCirculoPoligono(circRival, pol);

            } else {
                Polygon polRival = obtenerPoligonoBala(balaRival);
                if (pol != null && polRival != null)
                    return Intersector.overlapConvexPolygons(pol, polRival);
            }
        }
        return false;
    }

    public boolean comprobarColisiones(Bala bala, Personaje personaje) {

        if (bala instanceof BalaRect) {
            Rectangle rect = obtenerRectanguloBala(bala);
            if (personaje instanceof PersonajeCirc) {
                Circle circPersonaje = obtenerCirculoPersonaje(personaje);
                if (rect != null && circPersonaje != null)
                    return Intersector.overlaps(circPersonaje, rect);

            } else if (personaje instanceof PersonajePol) {
                Polygon polPersonaje = obtenerPoligonoPersonaje(personaje);
                if (rect != null && polPersonaje != null)
                    return chocaRectanguloPoligono(rect, polPersonaje, polPersonaje.getVertices());
            }

        } else if (bala instanceof BalaCirc) {
            Circle circ = obtenerCirculoBala(bala);
            if (personaje instanceof PersonajeCirc) {
                Circle circPersonaje = obtenerCirculoPersonaje(personaje);
                if (circ != null && circPersonaje != null)
                    return circ.overlaps(circPersonaje);
            } else if (personaje instanceof PersonajePol) {
                Polygon polPersonaje = obtenerPoligonoPersonaje(personaje);
                if (circ != null && polPersonaje != null)
                    return chocaCirculoPoligono(circ, polPersonaje);
            }

        } else if (bala instanceof BalaPol) {
            Polygon pol = obtenerPoligonoBala(bala);
            if (personaje instanceof PersonajeCirc) {
                Circle circPersonaje = obtenerCirculoPersonaje(personaje);
                if (pol != null && circPersonaje != null)
                    return chocaCirculoPoligono(circPersonaje, pol);

            } else if (personaje instanceof PersonajePol) {
                Polygon polPersonaje = obtenerPoligonoPersonaje(personaje);
                if (pol != null && polPersonaje != null)
                    return Intersector.overlapConvexPolygons(pol, polPersonaje);
            }
        }
        return false;
    }

    public Rectangle obtenerRectanguloBala(Bala bala) {
        return obtenerRectangulo(bala);
    }

    public Rectangle obtenerRectangulo(Object object) {
        try {
            Field field = object.getClass().getDeclaredField("rect");
            field.setAccessible(true);
            return (Rectangle) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Gdx.app.debug("DEBUG", "[ERROR Rectangulo] " + e.getMessage());
        }
        return null;
    }

    Polygon obtenerPoligonoBala(Bala bala) {
        return obtenerPoligono(bala);
    }

    Polygon obtenerPoligonoPersonaje(Personaje personaje) {
        return obtenerPoligono(personaje);
    }

    protected Polygon obtenerPoligono(Object object) {
        Field field;
        try {
            field = object.getClass().getDeclaredField("pol");
            field.setAccessible(true);
            return (Polygon) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Gdx.app.debug("DEBUG", "[ERROR Poligono] " + e.getMessage());
        }
        return null;
    }

    Circle obtenerCirculoBala(Bala bala) {
        return obtenerCirculo(bala);
    }

    Circle obtenerCirculoPersonaje(Personaje personaje) {
        return obtenerCirculo(personaje);
    }

    Circle obtenerCirculo(Object object) {
        try {
            Field field = object.getClass().getDeclaredField("circ");
            field.setAccessible(true);
            return (Circle) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Gdx.app.debug("DEBUG", "[ERROR Circulo]" + e.getMessage());
        }
        return null;
    }

    boolean chocaRectanguloPoligono(Rectangle rect, Polygon polRival, float[] vertices) {
        Polygon rPoly = new Polygon(vertices);
        rPoly.setPosition(rect.x, rect.y);

        return Intersector.overlapConvexPolygons(rPoly, polRival);
    }

    boolean chocaCirculoPoligono(Circle circ, Polygon pol) {
        centro.set(circ.x, circ.y);
        float[] vertices = pol.getVertices();
        float radioCuadrado = (float) Math.pow(circ.radius, 2d);
        if (Intersector.intersectSegmentCircle(vec1.set(vertices[vertices.length - 2], vertices[vertices.length - 1]), vec2.set(vertices[0], vertices[1]), centro, radioCuadrado))
            return true;
        for (int i = 2; i < vertices.length; i += 2)
            if (Intersector.intersectSegmentCircle(vec1.set(vertices[i - 2], vertices[i - 1]), vec2.set(vertices[i], vertices[i + 1]), centro, radioCuadrado))
                return true;
        return pol.contains(circ.x, circ.y);
    }



}
