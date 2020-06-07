package com.daisa.tfg.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class JuegoAssetManager {

    public final AssetManager managerJuego = new AssetManager();
    Skin skin;
    public final String skinNombre = "skin/glassy-ui.json";
    BitmapFont font;

    Label.LabelStyle estiloLabel;

    TextField.TextFieldStyle estiloDefecto, estiloTextField;

    public JuegoAssetManager() {
        if (!managerJuego.isLoaded(skinNombre)) {
            Gdx.app.debug("DEBUG", "LoginScreen::Skin no cargada");
            cargaSkin();
            managerJuego.finishLoading();
            Gdx.app.debug("DEBUG", "LoginScreen::Skin terminada de cargar");
        }

        skin = managerJuego.get("skin/glassy-ui.json");

        cargarFuente();
        cargarEstilosUI();
    }

    public void cargaSkin(){
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        managerJuego.load(skinNombre, Skin.class, params);
    }

    public void cargarFuente(){
        font = new BitmapFont(Gdx.files.internal("Fuentes/fuente.fnt"), false);
    }

    public void cargarEstilosUI(){
        estiloDefecto = skin.get("default", TextField.TextFieldStyle.class);
        estiloTextField = new TextField.TextFieldStyle(font, estiloDefecto.fontColor, estiloDefecto.cursor, estiloDefecto.selection, estiloDefecto.background);

        estiloLabel = new Label.LabelStyle();
        estiloLabel.font = font;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Label.LabelStyle getEstiloLabel() {
        return estiloLabel;
    }

    public Skin getSkin() {
        return skin;
    }

    public TextField.TextFieldStyle getEstiloTextField() {
        return estiloTextField;
    }
}
