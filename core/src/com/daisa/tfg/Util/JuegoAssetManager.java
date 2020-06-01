package com.daisa.tfg.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class JuegoAssetManager {

    public final AssetManager managerJuego = new AssetManager();
    public final String skin = "skin/glassy-ui.json";

    BitmapFont font;

    public void cargaSkin(){
        SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
        managerJuego.load(skin, Skin.class, params);
    }

    public void cargarFuente(){
        font = new BitmapFont(Gdx.files.internal("Font/fuente2.fnt"), false);
    }

}
