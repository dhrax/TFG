package com.daisa.tfg.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.daisa.tfg.Constantes.ConstantesJuego;
import com.daisa.tfg.Principal.Juego;

public class ElegirModoScreen implements Screen {

    Stage stage;
    Juego juego;

    TextButton btJugar;
    TextButton btAjustes;

    public ElegirModoScreen(Juego juego) {
        this.juego = juego;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    private void cambiarMusica() {
        if(juego.manager.managerJuego.get(ConstantesJuego.MUSICA_JUEGO, Music.class).isPlaying()){
            juego.manager.managerJuego.get(ConstantesJuego.MUSICA_JUEGO, Music.class).stop();
        }
        juego.reproducirMusica(juego.manager.managerJuego.get(ConstantesJuego.MUSICA_MENU, Music.class));
    }

    @Override
    public void show() {
        cambiarMusica();

        stage = new Stage(juego.viewport);

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);
        tabla.setBackground(new TiledDrawable(juego.getFondoMenu()));

        btJugar = new TextButton("Cooperativo", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class));
        btJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.reproducirSonido(juego.manager.managerJuego.get(ConstantesJuego.SONIDO_PULSAR_BOTON, Sound.class));
                Gdx.app.debug("DEBUG", "ElegirModoScreen::Se comprueba si el Bluetooth esta encendido");
                if(juego.estaBluetoothEncencido()){
                    Gdx.app.debug("DEBUG", "ElegirModoScreen::Bluetooth encendido, se crea la Screen ConectarJugadoresScreen");
                    juego.conectarJugadoresScreen = new ConectarJugadoresScreen(juego);
                    juego.setScreen(juego.conectarJugadoresScreen);
                }else{
                    Gdx.app.debug("DEBUG", "ElegirModoScreen::Bluetooth apagado, se pide permiso para encenderlo");
                    juego.activarBluetooth();
                }
            }
        });

        btAjustes = new TextButton("Tutorial", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class));
        btAjustes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.reproducirSonido(juego.manager.managerJuego.get(ConstantesJuego.SONIDO_PULSAR_BOTON, Sound.class));
                //TODO crear un tutorial
                //Gdx.app.debug("DEBUG", "ElegirModoScreen::Se lanza el tutorial");
            }
        });

        tabla.row().padBottom(30).width(700).height(120);
        tabla.add(btJugar);

        tabla.row().padBottom(30).width(700).height(120);
        tabla.add(btAjustes);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        juego.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
