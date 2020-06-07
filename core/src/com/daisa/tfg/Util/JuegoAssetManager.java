package com.daisa.tfg.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.daisa.tfg.Constantes.ConstantesJuego;

public class JuegoAssetManager {

    public final AssetManager managerJuego = new AssetManager();

    Label.LabelStyle estiloLabel;

    TextField.TextFieldStyle estiloDefecto, estiloTextField;

    public JuegoAssetManager() {
        cargaSkin();
        cargarFuente();
        cargarSonidos();
        cargarImagenes();

        managerJuego.finishLoading();

        managerJuego.get(ConstantesJuego.MUSICA_MENU, Music.class).setLooping(true);
        managerJuego.get(ConstantesJuego.MUSICA_JUEGO, Music.class).setLooping(true);
        cargarEstilosUI();
    }

    private void cargarImagenes() {
        managerJuego.load(ConstantesJuego.IMAGEN_LOGO, Texture.class);
        managerJuego.load(ConstantesJuego.IMAGEN_EXCLAMACION, Texture.class);
    }

    private void cargarSonidos() {
        managerJuego.load(ConstantesJuego.SONIDO_PULSAR_BOTON, Sound.class);
        managerJuego.load(ConstantesJuego.SONIDO_TRANSICION, Sound.class);
        managerJuego.load(ConstantesJuego.SONIDO_FIN_PARTIDA, Sound.class);
        for (String nombre : ConstantesJuego.ARRAY_SONIDOS_DISPARO){
            managerJuego.load(ConstantesJuego.ARRAY_SONIDOS_DISPARO.get(ConstantesJuego.ARRAY_SONIDOS_DISPARO.indexOf(nombre, false)), Sound.class);
        }
        for (String nombre : ConstantesJuego.ARRAY_SONIDOS_CARGA){
            managerJuego.load(ConstantesJuego.ARRAY_SONIDOS_CARGA.get(ConstantesJuego.ARRAY_SONIDOS_CARGA.indexOf(nombre, false)), Sound.class);
        }
        for (String nombre : ConstantesJuego.ARRAY_SONIDOS_GOLPE){
            managerJuego.load(ConstantesJuego.ARRAY_SONIDOS_GOLPE.get(ConstantesJuego.ARRAY_SONIDOS_GOLPE.indexOf(nombre, false)), Sound.class);
        }
        managerJuego.load(ConstantesJuego.MUSICA_MENU, Music.class);
        managerJuego.load(ConstantesJuego.MUSICA_JUEGO, Music.class);
    }

    public void cargaSkin(){
        SkinParameter params = new SkinParameter(ConstantesJuego.NOMBRE_ATLAS_SKIN);
        managerJuego.load(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class, params);
    }

    public void cargarFuente(){
        managerJuego.load(ConstantesJuego.FUENTE, BitmapFont.class);
    }

    public void cargarEstilosUI(){
        estiloDefecto = managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class).get("default", TextField.TextFieldStyle.class);
        estiloTextField = new TextField.TextFieldStyle(managerJuego.get(ConstantesJuego.FUENTE, BitmapFont.class), estiloDefecto.fontColor, estiloDefecto.cursor, estiloDefecto.selection, estiloDefecto.background);

        estiloLabel = new Label.LabelStyle();
        estiloLabel.font = managerJuego.get(ConstantesJuego.FUENTE, BitmapFont.class);
    }

    public Label.LabelStyle getEstiloLabel() {
        return estiloLabel;
    }

    public TextField.TextFieldStyle getEstiloTextField() {
        return estiloTextField;
    }

    public AssetManager getManagerJuego() {
        return managerJuego;
    }
}
